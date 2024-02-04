package ru.geekbrains.movies.ui.movies.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.geekbrains.movies.BuildConfig
import ru.geekbrains.movies.R
import ru.geekbrains.movies.databinding.MovieItemBinding
import ru.geekbrains.movies.domain.models.MoviesResponse
import ru.geekbrains.movies.utils.click
import ru.geekbrains.movies.utils.getColorByValue
import ru.geekbrains.movies.utils.releaseDateToString
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlin.math.roundToInt

class MovieViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    private val viewBinding: MovieItemBinding by viewBinding()

    fun bind(
        movie: MoviesResponse.Movie,
        delegate: MovieAdapter.Delegate?,
        position: Int,
        countItems: Int
    ) {
        with(viewBinding) {
            title.text = movie.title
            val popular = (movie.voteAverage * MULTIPLIER).roundToInt()
            ratingProgress.setProgress(popular, true)
            ratingValue.text = popular.toString()
            ratingProgress.setIndicatorColor(getColorByValue(popular))
            if (movie.releaseDate.isNullOrEmpty().not()) {
                release.text = releaseDateToString(movie.releaseDate)
            }
            Glide.with(poster)
                .load(BuildConfig.MOVIE_POSTER_PATH.plus(movie.posterPath))
                .apply(RequestOptions.bitmapTransform(RoundedCorners(IMAGE_RADIUS)))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(DrawableTransitionOptions().crossFade(DELAY))
                .placeholder(R.drawable.ic_no_image)
                .error(R.drawable.ic_no_image)
                .into(poster)
            viewBinding.root.click { delegate?.onItemClick(movie) }
            if (countItems > ZERO_VALUE && position == countItems - FIVE_VALUE) {
                delegate?.getMoreMovies()
            }
        }
    }

    companion object {
        private const val MULTIPLIER = 10
        private const val IMAGE_RADIUS = 18
        private const val ZERO_VALUE = 0
        private const val FIVE_VALUE = 5
        private const val DELAY = 800
    }
}