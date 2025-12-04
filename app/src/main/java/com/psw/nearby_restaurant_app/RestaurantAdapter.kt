package com.psw.nearby_restaurant_app

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.psw.nearby_restaurant_app.data.Restaurant
import com.psw.nearby_restaurant_app.databinding.ItemRestaurantBinding

class RestaurantAdapter(private val restaurantList: List<Restaurant>)
    : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() { // 내부 클래스로 변경

    // 2. 뷰 홀더 정의
    inner class RestaurantViewHolder(private val binding: ItemRestaurantBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: Restaurant) {

            //  뷰에 데이터 바인딩
            binding.tvRestaurantName.text = restaurant.name
            binding.tvRestaurantAddress.text = restaurant.address

            binding.tvRating.text = String.format("%.1f점", restaurant.rating)

            binding.tvFoodType.text = restaurant.foodType

            //  이미지 처리 (실제 사용 시 Glide 또는 Coil 라이브러리 추가 필요)
            // if (restaurant.photoUrl.isNotEmpty()) {
            //     Glide.with(binding.ivRestaurantPhoto.context)
            //          .load(restaurant.photoUrl)
            //          .into(binding.ivRestaurantPhoto)
            // }

            //  상세보기 버튼 클릭 리스너 설정
            binding.bDetailShow.setOnClickListener {
                Toast.makeText(it.context, "${restaurant.name}의 상세 정보를 봅니다.", Toast.LENGTH_SHORT).show()
                // 여기에 상세 화면 이동 로직을 구현합니다.
            }

            // 아이템 전체 클릭 리스너 설정 (선택 사항)
            binding.root.setOnClickListener {
                Toast.makeText(it.context, "${restaurant.name} 아이템 클릭됨", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 3. 뷰 홀더 생성: 레이아웃 인플레이트 및 바인딩 객체 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        // XML 파일 이름이 item_restaurant.xml이라고 가정합니다.
        val binding = ItemRestaurantBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RestaurantViewHolder(binding)
    }

    // 4. 데이터 바인딩: 홀더에 데이터 전달
    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.bind(restaurant)
    }

    // 5. 아이템 개수 반환
    override fun getItemCount(): Int = restaurantList.size
}