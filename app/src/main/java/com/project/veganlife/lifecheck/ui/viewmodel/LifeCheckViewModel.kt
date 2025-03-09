package com.project.veganlife.lifecheck.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.project.veganlife.data.model.ApiResult
import com.project.veganlife.data.model.DailyIntakeResponse
import com.project.veganlife.data.model.RecommendedIntakeResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealData
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataDetail
import com.project.veganlife.lifecheck.data.model.LifeCheckMealDataRequest
import com.project.veganlife.lifecheck.data.model.LifeCheckMealLogDetailResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckMealLogListResponse
import com.project.veganlife.lifecheck.data.model.LifeCheckWeeklyCalorieResponse
import com.project.veganlife.lifecheck.domain.usecase.LifeCheckUseCase
import com.project.veganlife.lifecheck.util.EventWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class LifeCheckViewModel @Inject constructor(
    private val lifeCheckUseCase: LifeCheckUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // 일일 섭취량 조회
    private val _dailyIntakeData = MutableLiveData<ApiResult<DailyIntakeResponse>>()
    val dailyIntakeData: LiveData<ApiResult<DailyIntakeResponse>> = _dailyIntakeData

    // 조회 날짜
    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> = _selectedDate

    // 권장 섭취량 데이터
    private val _recommendedIntakeData = MutableLiveData<ApiResult<RecommendedIntakeResponse>>()
    val recommendedIntakeData: LiveData<ApiResult<RecommendedIntakeResponse>> =
        _recommendedIntakeData

    // 주간 섭취 칼로리 조회
    private val _weeklyCalorieData = MutableLiveData<ApiResult<LifeCheckWeeklyCalorieResponse>>()
    val weeklyCalorieData: LiveData<ApiResult<LifeCheckWeeklyCalorieResponse>> = _weeklyCalorieData

    // 조회 기간 설정
    private val _selectedWeeklyStartDate = MutableLiveData<String>()
    val selectedWeeklyStartDate: LiveData<String> = _selectedWeeklyStartDate

    private val _selectedWeeklyEndDate = MutableLiveData<String>()
    val selectedWeeklyEndDate: LiveData<String> = _selectedWeeklyEndDate

    // 월간 섭취 칼로리 조회
    private val _monthlyCalorieData = MutableLiveData<ApiResult<LifeCheckWeeklyCalorieResponse>>()
    val monthlyCalorieData: LiveData<ApiResult<LifeCheckWeeklyCalorieResponse>> =
        _monthlyCalorieData

    // 연간 섭취 칼로리 조회
    private val _yearlyCalorieData = MutableLiveData<ApiResult<LifeCheckWeeklyCalorieResponse>>()
    val yearlyCalorieData: LiveData<ApiResult<LifeCheckWeeklyCalorieResponse>> = _yearlyCalorieData

    // 식사 종류 선택
    private val _selectedDietType = MutableLiveData<String>()
    val selectedDietType: LiveData<String> = _selectedDietType

    // 키워드 기반 식품 데이터 조회
    private val _mealData = MutableStateFlow<PagingData<LifeCheckMealData>>(PagingData.empty())
    val mealData: StateFlow<PagingData<LifeCheckMealData>> = _mealData

    // 식품 데이터 등록
    private val _mealDataRegister =
        MutableLiveData<EventWrapper<ApiResult<Unit>>>()
    val mealDataRegister: LiveData<EventWrapper<ApiResult<Unit>>> =
        _mealDataRegister

    // ID 식품데이터 조회
    private val _mealDataById = MutableLiveData<EventWrapper<ApiResult<LifeCheckMealDataDetail>>>()
    val mealDataById: LiveData<EventWrapper<ApiResult<LifeCheckMealDataDetail>>> = _mealDataById

    // 식품데이터 수정
    private val _mealDataUpdateResult =
        MutableLiveData<EventWrapper<ApiResult<Unit>>>()
    val mealDataUpdateResult: LiveData<EventWrapper<ApiResult<Unit>>> = _mealDataUpdateResult

    // 식품데이터 삭제
    private val _mealDataDeleteResult =
        MutableLiveData<EventWrapper<ApiResult<Unit>>>()
    val mealDataDeleteResult: LiveData<EventWrapper<ApiResult<Unit>>> = _mealDataDeleteResult

    // 식사 기록 등록
    private val _registerMealLogResult = MutableLiveData<EventWrapper<ApiResult<Unit>>>()
    val registerMealLogResult: LiveData<EventWrapper<ApiResult<Unit>>> = _registerMealLogResult

    // 동적 뷰 관리 리스트 추가
    private val _dynamicMealList =
        savedStateHandle.getLiveData<MutableList<LifeCheckMealDataDetail>>(
            "dynamicMealList",
            mutableListOf()
        )
    val dynamicMealList: LiveData<MutableList<LifeCheckMealDataDetail>> = _dynamicMealList

    // intakeValue 관리
    private val _intakeValues = MutableLiveData<Map<Long, Double>>(emptyMap())
    val intakeValues: LiveData<Map<Long, Double>> = _intakeValues

    // 음식 ID
    private val _selectedMealId = MutableLiveData<Long>()
    val selectedMealId: LiveData<Long> = _selectedMealId

    // 식사 기록 사진 리스트 관리
    private val _selectedPhotos = MutableLiveData<MutableList<Uri>>(mutableListOf())
    val selectedPhotos: MutableLiveData<MutableList<Uri>> = _selectedPhotos

    // 식사 기록 목록
    private val _mealLogList =
        MutableLiveData<EventWrapper<ApiResult<List<LifeCheckMealLogListResponse>>>>()
    val mealLogList: LiveData<EventWrapper<ApiResult<List<LifeCheckMealLogListResponse>>>> =
        _mealLogList

    // 식사 기록 상세 조회
    private val _mealLogDetail =
        MutableLiveData<EventWrapper<ApiResult<LifeCheckMealLogDetailResponse>>>()
    val mealLogDetail: LiveData<EventWrapper<ApiResult<LifeCheckMealLogDetailResponse>>> =
        _mealLogDetail

    // 서버에서 불러온 사진 리스트
    private val _serverPhotoList = MutableStateFlow<List<Uri>>(emptyList())
    val serverPhotoList: StateFlow<List<Uri>> = _serverPhotoList.asStateFlow()

    // 사용자가 추가한 사진 리스트
    private val _userPhotoList = MutableStateFlow<List<Uri>>(emptyList())
    val userPhotoList: StateFlow<List<Uri>> = _userPhotoList.asStateFlow()

    private val _modifyMealLogResult = MutableLiveData<EventWrapper<ApiResult<Unit>>>()
    val modifyMealLogResult: LiveData<EventWrapper<ApiResult<Unit>>> = _modifyMealLogResult

    private val _mealLogId = savedStateHandle.getLiveData<Long>("mealLogId")
    val mealLogId: LiveData<Long> = _mealLogId

    // 식사기록 삭제
    private val _mealLogDeleteResult =
        MutableLiveData<EventWrapper<ApiResult<Unit>>>()
    val mealLogDeleteResult: LiveData<EventWrapper<ApiResult<Unit>>> = _mealLogDeleteResult

    // 일일 섭취량 조회
    fun fetchDailyIntake(date: String) {
        viewModelScope.launch {
            _dailyIntakeData.value = lifeCheckUseCase.getDailyIntake(date)
        }
    }

    fun updateDate(date: String) {
        _selectedDate.value = date
    }

    // 권장 섭취량 조회
    fun fetchRecommendedIntake() {
        viewModelScope.launch {
            _recommendedIntakeData.value = lifeCheckUseCase.getRecommendedIntake()
        }
    }

    // 주간 섭취 칼로리 조회
    fun fetchWeeklyCalorie(startDate: String, endDate: String) {
        viewModelScope.launch {
            _weeklyCalorieData.value = lifeCheckUseCase.getWeeklyCalorie(startDate, endDate)
        }
    }

    fun updateWeeklyStartDate(date: String) {
        _selectedWeeklyStartDate.value = date
    }

    fun updateWeeklyEndDate(date: String) {
        _selectedWeeklyEndDate.value = date
    }

    // 월간 섭취 칼로리 조회
    fun fetchMonthlyCalorie(startDate: String) {
        viewModelScope.launch {
            _monthlyCalorieData.value = lifeCheckUseCase.getMonthlyCalorie(startDate)
        }
    }

    // 연간 섭취 칼로리 조회
    fun fetchYearlyCalorie(startDate: String) {
        viewModelScope.launch {
            _yearlyCalorieData.value = lifeCheckUseCase.getYearlyCalorie(startDate)
        }
    }

    // 식사 종류 선택
    fun setSelectedDietType(dietType: String) {
        _selectedDietType.value = dietType
    }

    // 키워드 기반 식품 데이터 조회
    fun searchMealData(keyword: String, ownerType: String) {
        viewModelScope.launch {
            lifeCheckUseCase.getMealDataStream(keyword, ownerType).cachedIn(viewModelScope)
                .collectLatest {
                    _mealData.value = it
                }
        }
    }

    // 식품 데이터 등록
    fun registerMealData(lifeCheckMealDataRequest: LifeCheckMealDataRequest) {
        viewModelScope.launch {
            _mealDataRegister.value =
                EventWrapper(lifeCheckUseCase.registerMealData(lifeCheckMealDataRequest))
        }
    }

    fun fetchMealDataById(id: Long) {
        viewModelScope.launch {
            _mealDataById.value = EventWrapper(lifeCheckUseCase.getMealDataById(id))
        }
    }

    fun modifyMealData(id: Long, updatedData: LifeCheckMealDataRequest) {
        viewModelScope.launch {
            _mealDataUpdateResult.value =
                EventWrapper(lifeCheckUseCase.modifyMealData(id, updatedData))
        }
    }

    fun deleteMealData(id: Long) {
        viewModelScope.launch {
            _mealDataDeleteResult.value = EventWrapper(lifeCheckUseCase.deleteMealData(id))
        }
    }

    // 음식 리스트 데이터 추가
    fun addMealData(mealData: LifeCheckMealDataDetail) {
        val updatedList = _dynamicMealList.value ?: mutableListOf()
        updatedList.add(mealData)
        _dynamicMealList.value = updatedList
        savedStateHandle["dynamicMealList"] = updatedList
    }

    fun addMealDataList(mealDataList: List<LifeCheckMealDataDetail>) {
        val currentList = _dynamicMealList.value ?: mutableListOf()
        currentList.addAll(mealDataList)
        _dynamicMealList.value = currentList
        savedStateHandle["dynamicMealList"] = currentList
    }

    // 음식 리스트 데이터 삭제
    fun removeMealData(mealData: LifeCheckMealDataDetail) {
        val updatedList = _dynamicMealList.value ?: mutableListOf()
        updatedList.remove(mealData)
        _dynamicMealList.value = updatedList
        savedStateHandle["dynamicMealList"] = updatedList
    }

    // 음식 리스트 저장된 데이터를 로드하여 동적 뷰 복원
    fun getMealDataList(): List<LifeCheckMealDataDetail> {
        return _dynamicMealList.value ?: emptyList()
    }

    // 동적 뷰 관리 리스트 초기화 함수
    fun clearDynamicMealList() {
        _dynamicMealList.value = mutableListOf<LifeCheckMealDataDetail>()
        savedStateHandle["dynamicMealList"] = mutableListOf<LifeCheckMealDataDetail>()
    }

    fun getIntakeValue(mealId: Long): Double {
        return _intakeValues.value?.get(mealId) ?: 1.0
    }

    fun setIntakeValue(mealId: Long, value: Double) {
        val currentMap = _intakeValues.value?.toMutableMap() ?: mutableMapOf()
        currentMap[mealId] = value
        _intakeValues.value = currentMap
    }

    fun removeIntakeValue(mealId: Long) {
        val currentMap = _intakeValues.value?.toMutableMap() ?: mutableMapOf()
        currentMap.remove(mealId)
        _intakeValues.value = currentMap
    }

    fun registerMealLog(
        mealLogRequest: RequestBody,
        images: List<MultipartBody.Part>
    ) {
        viewModelScope.launch {
            _registerMealLogResult.value =
                EventWrapper(lifeCheckUseCase.registerMealLog(mealLogRequest, images))
        }
    }

    fun fetchMealLogList(date: String) {
        viewModelScope.launch {
            _mealLogList.value = EventWrapper(lifeCheckUseCase.getMealLogList(date))
        }
    }

    fun fetchMealLogDetail(mealLogId: Long) {
        viewModelScope.launch {
            _mealLogDetail.value = EventWrapper(lifeCheckUseCase.getMealLogDetail(mealLogId))
        }
    }

    // 서버 사진 리스트 업데이트
    fun setServerPhotoList(list: List<Uri>) {
        _serverPhotoList.value = list
    }

    // 사용자 사진 리스트 업데이트
    fun setUserPhotoList(list: List<Uri>) {
        _userPhotoList.value = list
    }

    // 특정 사진 삭제
    fun removePhoto(uri: Uri) {
        if (_serverPhotoList.value.contains(uri)) {
            _serverPhotoList.value = _serverPhotoList.value - uri
        } else if (_userPhotoList.value.contains(uri)) {
            _userPhotoList.value = _userPhotoList.value - uri
        }
    }

    fun modifyMealLog(
        mealLogId: Long,
        mealLogRequest: RequestBody,
        images: List<MultipartBody.Part>
    ) {
        viewModelScope.launch {
            _modifyMealLogResult.value =
                EventWrapper(lifeCheckUseCase.modifyMealLog(mealLogId, mealLogRequest, images))
        }
    }

    fun setMealLogId(id: Long) {
        if (_mealLogId.value == null) {
            _mealLogId.value = id
            savedStateHandle["mealLogId"] = id
        }
    }

    fun deleteMealLog(mealLogId: Long) {
        viewModelScope.launch {
            _mealLogDeleteResult.value = EventWrapper(lifeCheckUseCase.deleteMealLog(mealLogId))
        }
    }
}