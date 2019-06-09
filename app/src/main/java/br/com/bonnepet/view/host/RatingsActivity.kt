package br.com.bonnepet.view.host

import Data
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.model.RateDetailsDTO
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.host.adapter.RatingAdapter
import kotlinx.android.synthetic.main.activity_ratings.*

class RatingsActivity : BaseActivity() {
    override val layoutResource = R.layout.activity_ratings

    override val activityTitle = R.string.ratings

    private val recyclerView by lazy { recycler_view }

    private lateinit var ratingAdapter: RatingAdapter

    private lateinit var ratings: List<RateDetailsDTO>

    override fun onPrepareActivity(state: Bundle?) {
        ratings = intent.getSerializableExtra(Data.RATING_DETAIL_DTO) as List<RateDetailsDTO>

        ratingAdapter = RatingAdapter(this, ratings.toMutableList())
        recyclerView.adapter = ratingAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    /**
     *  Ação do botão de voltar da actionBar
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
