package com.example.YounhongStagram.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.YounhongStagram.R
import com.example.YounhongStagram.navigation.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_grid.view.*

class GridFragment : Fragment() {
    var firestore : FirebaseFirestore? = null
    var fragmentView : View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = LayoutInflater.from(activity).inflate(R.layout.fragment_grid, container, false)
        firestore = FirebaseFirestore.getInstance()
        fragmentView?.grid_fragment_recyclerview?.adapter = UserFragmentRecyclerViewAdapter()
        fragmentView?.grid_fragment_recyclerview?.layoutManager = GridLayoutManager(activity, 3)

        return fragmentView
    }

    inner class UserFragmentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var contentDTO : ArrayList<ContentDTO> = arrayListOf()
        init {
            firestore?.collection("images")?.addSnapshotListener {
                    querySnapshot, firebaseFirestoreException ->
                    if (querySnapshot == null) return@addSnapshotListener
                    for (snapshot in querySnapshot.documents) {
                        contentDTO.add(snapshot.toObject(ContentDTO::class.java)!!)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var width = resources.displayMetrics.widthPixels / 3
            var imageview  = ImageView(parent.context)
            imageview.layoutParams = LinearLayoutCompat.LayoutParams(width, width)

            return CustomViewHolder(imageview)
        }

        private inner class CustomViewHolder(var imageview: ImageView) : RecyclerView.ViewHolder(imageview)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var imageView = (holder as CustomViewHolder).imageview
            Glide.with(holder.itemView.context).load(contentDTO[position].imageUrl)
                .apply(RequestOptions().centerCrop()).into(imageView)
        }

        override fun getItemCount(): Int {
            return contentDTO.size
        }
    }
}