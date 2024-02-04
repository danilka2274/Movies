package ru.geekbrains.movies.ui.detail.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import ru.geekbrains.movies.BuildConfig
import ru.geekbrains.movies.R
import ru.geekbrains.movies.databinding.ActorItemBinding
import ru.geekbrains.movies.domain.models.ActorsResponse
import ru.geekbrains.movies.utils.click

/**
 * @author Borisov Andrey on 29.06.2022
 **/
class ActorViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    private val viewBinding: ActorItemBinding by viewBinding()

    fun bind(actor: ActorsResponse.Cast, delegate: ActorAdapter.Delegate?) {
        with(viewBinding) {
            name.text = actor.name
            character.text = actor.character
            Glide.with(poster)
                .load(BuildConfig.MOVIE_POSTER_PATH.plus(actor.profilePath))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(IMAGE_RADIUS)))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(DrawableTransitionOptions().crossFade(DELAY))
                .placeholder(R.drawable.ic_no_image)
                .error(R.drawable.ic_no_image)
                .into(poster)
            viewBinding.root.click { delegate?.onItemClick(actor) }
        }
    }

    companion object {
        private const val DELAY = 800
        private const val IMAGE_RADIUS = 18
    }
}