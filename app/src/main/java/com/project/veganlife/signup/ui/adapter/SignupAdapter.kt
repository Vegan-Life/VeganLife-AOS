package com.project.veganlife.signup.ui.adapter

import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.veganlife.R
import com.project.veganlife.databinding.ItemRecyclerviewSignupVeganTypeBinding
import com.project.veganlife.signup.data.model.SignupVeganType
import com.project.veganlife.signup.ui.viewmodel.SignupVeganTypeViewModel

class SignupAdapter(
    private val viewModel: SignupVeganTypeViewModel
) : ListAdapter<SignupVeganType, SignupAdapter.SignupViewHolder>(diffUtil) {
    private var selectedPos: Int = RecyclerView.NO_POSITION

    inner class SignupViewHolder(val binding: ItemRecyclerviewSignupVeganTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.clSignupLayout.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (selectedPos != position) {
                        val previouslySelectedPos = selectedPos // 이전에 선택된 아이템의 위치를 저장
                        selectedPos = position // 새로운 선택된 아이템의 위치를 업데이트
                        notifyItemChanged(previouslySelectedPos) // 이전에 선택된 아이템의 UI를 업데이트하여 선택 취소
                        notifyItemChanged(selectedPos) // 새로 선택된 아이템의 UI를 업데이트하여 선택 상태 표시

                        val item = getItem(position)
                        viewModel.setSelectedVeganName(item.koreanVeganName)
                    } else {
                        // 이미 선택된 아이템을 다시 클릭한 경우 선택 취소 처리
                        selectedPos = RecyclerView.NO_POSITION
                        notifyItemChanged(position)
                        viewModel.clearSelectedVeganName() // 선택된 아이템의 정보를 ViewModel에서 제거
                    }
                }
            }
        }

        fun bind(item: SignupVeganType) {
            binding.apply {
                if (adapterPosition == selectedPos) {
                    clSignupLayout.setBackgroundResource(R.drawable.signup_vegan_type_selected)
                    tvSignupVeganType.setTextColor(
                        ContextCompat.getColor(
                            tvSignupVeganType.context,
                            R.color.gray1
                        )
                    )
                    tvSignupVeganTypeEng.setTextColor(
                        ContextCompat.getColor(
                            tvSignupVeganType.context,
                            R.color.gray1
                        )
                    )
                } else {
                    clSignupLayout.setBackgroundResource(R.drawable.signup_vegan_type)
                    tvSignupVeganType.setTextColor(
                        ContextCompat.getColor(
                            tvSignupVeganType.context,
                            R.color.base3
                        )
                    )
                    tvSignupVeganTypeEng.setTextColor(
                        ContextCompat.getColor(
                            tvSignupVeganType.context,
                            R.color.base3
                        )
                    )
                }

                Glide.with(binding.ivSignupImage)
                    .load(item.imageVeganType)
                    .into(ivSignupImage)

                tvSignupVeganType.text = item.koreanVeganName
                tvSignupVeganTypeEng.text = item.englishVeganName
                tvSignupVeganTypeDescription.text = itemView.context.getString(item.explainVegan)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SignupViewHolder {
        val itemSignupVeganTypeBinding = ItemRecyclerviewSignupVeganTypeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )

        return SignupViewHolder(itemSignupVeganTypeBinding)
    }

    override fun onBindViewHolder(
        holder: SignupViewHolder,
        position: Int,
    ) {
        val item = currentList[position]
        holder.bind(item)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SignupVeganType>() {
            override fun areItemsTheSame(
                oldItem: SignupVeganType,
                newItem: SignupVeganType,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SignupVeganType,
                newItem: SignupVeganType,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
