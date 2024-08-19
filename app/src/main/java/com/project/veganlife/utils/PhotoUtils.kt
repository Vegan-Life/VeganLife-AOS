package com.project.veganlife.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PhotoUtils {
    companion object {
        private const val MAX_WIDTH = 1280
        private const val MAX_HEIGHT = 960

        /**
         * 최적화된 Bitmap을 생성하고 임시 파일로 저장 후 해당 파일의 경로를 반환합니다.
         * @param context 컨텍스트
         * @param uri 이미지의 URI
         * @return 최적화된 이미지의 임시 파일 경로
         */
        fun optimizeBitmap(context: Context, uri: Uri): String? {
            return try {
                // 임시 파일을 위한 디렉토리와 파일명 설정
                val storage = context.cacheDir
                val fileName = String.format("%s.%s", UUID.randomUUID(), "jpg")
                val tempFile = File(storage, fileName)
                tempFile.createNewFile()

                // 파일 출력 스트림 생성
                val fos = FileOutputStream(tempFile)

                // Bitmap을 디코딩 및 최적화 후 임시 파일로 저장
                decodeBitmapFromUri(uri, context)?.apply {
                    val format = when(getFileExtension(uri, context)) {
                        "png" -> Bitmap.CompressFormat.PNG
                        else -> Bitmap.CompressFormat.JPEG
                    }
                    compress(format, 100, fos)
                    recycle()
                } ?: throw NullPointerException("Bitmap decoding failed")

                fos.flush()
                fos.close()

                return tempFile.absolutePath

            } catch (e: Exception) {
                Log.e("PhotoUtils", "FileUtil - ${e.message}")
                null
            }
        }

        /**
         * URI로부터 Bitmap을 디코딩하고 최적화된 Bitmap을 반환합니다.
         * @param uri 이미지의 URI
         * @param context 컨텍스트
         * @return 최적화된 Bitmap
         */
        private fun decodeBitmapFromUri(uri: Uri, context: Context): Bitmap? {
            val input = BufferedInputStream(context.contentResolver.openInputStream(uri))

            input.mark(input.available()) // 스트림의 현재 위치를 표시

            var bitmap: Bitmap?

            BitmapFactory.Options().run {
                inJustDecodeBounds = true // 이미지 실제 로딩 없이 크기만 가져오기
                bitmap = BitmapFactory.decodeStream(input, null, this)

                input.reset() // 스트림을 초기 위치로 되돌리기

                // 이미지 리샘플링을 위해 inSampleSize 계산
                inSampleSize = calculateInSampleSize(this)
                inJustDecodeBounds = false

                bitmap = BitmapFactory.decodeStream(input, null, this)?.apply {
                    rotateImageIfRequired(context, this, uri)
                }
            }

            input.close()

            return bitmap
        }

        /**
         * 최적화된 Bitmap을 위한 inSampleSize를 계산합니다.
         * @param options BitmapFactory.Options
         * @return inSampleSize 값
         */
        private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
            val (height: Int, width: Int) = options.run { outHeight to outWidth }
            var inSampleSize = 1

            if (height > MAX_HEIGHT || width > MAX_WIDTH) {
                val halfHeight: Int = height / 2
                val halfWidth: Int = width / 2

                while (halfHeight / inSampleSize >= MAX_HEIGHT && halfWidth / inSampleSize >= MAX_WIDTH) {
                    inSampleSize *= 2
                }
            }

            return inSampleSize
        }

        /**
         * 이미지가 회전된 경우 이를 원래 방향으로 되돌립니다.
         * @param context 컨텍스트
         * @param bitmap 회전된 Bitmap
         * @param uri 이미지의 URI
         * @return 원래 방향으로 회전된 Bitmap
         */
        private fun rotateImageIfRequired(context: Context, bitmap: Bitmap, uri: Uri): Bitmap? {
            val input = context.contentResolver.openInputStream(uri) ?: return null

            val exif = if (Build.VERSION.SDK_INT > 23) {
                ExifInterface(input)
            } else {
                ExifInterface(uri.path!!)
            }

            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            return when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
                else -> bitmap
            }
        }

        /**
         * Bitmap을 주어진 각도만큼 회전시킵니다.
         * @param bitmap 회전할 Bitmap
         * @param degree 회전 각도
         * @return 회전된 Bitmap
         */
        private fun rotateImage(bitmap: Bitmap, degree: Int): Bitmap? {
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }

        // URI에서 파일 확장자 추출
        fun getFileExtension(uri: Uri, context: Context): String? {
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(uri)
            return mimeType?.substringAfterLast('/')
        }

        /**
         * 이미지 파일을 MultipartBody.Part로 변환합니다.
         * @param imagePath 최적화된 이미지 파일 경로
         * @return MultipartBody.Part로 변환된 이미지 파일
         */
        fun createImageMultipart(imagePath: String?): MultipartBody.Part? {
            if (imagePath == null) return null

            val file = File(imagePath)
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            return MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        /**
         * ProfileModifyInfo 객체를 JSON으로 변환하여 RequestBody로 반환합니다.
         * @param profileModifyInfo 서버에 보낼 프로필 정보
         * @return RequestBody로 변환된 프로필 정보
         */
        fun createProfileRequestBody(contentDTO: Any): RequestBody {
            val gson = Gson()
            val json = gson.toJson(contentDTO)
            return json.toRequestBody("application/json".toMediaTypeOrNull())
        }
    }
}