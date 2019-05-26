package br.com.bonnepet.view.host

import Data
import RequestCode
import Time
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bonnepet.R
import br.com.bonnepet.data.enums.PetSizeEnum
import br.com.bonnepet.data.model.HostDTO
import br.com.bonnepet.data.model.PetDTO
import br.com.bonnepet.util.data.SessionManager
import br.com.bonnepet.util.extension.isVisible
import br.com.bonnepet.util.extension.setSafeOnClickListener
import br.com.bonnepet.view.base.BaseActivity
import br.com.bonnepet.view.component.CircularProgressBar
import br.com.bonnepet.view.booking.BookDetailsActivity
import br.com.bonnepet.view.login.LoginActivity
import br.com.bonnepet.view.pet.PetDetailsActivity
import br.com.bonnepet.view.pet.adapter.PetAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_host_details.*

class HostDetailsActivity : BaseActivity(), PetAdapter.ItemClickListener {
    override val layoutResource = R.layout.activity_host_details
    override val activityTitle = R.string.host_details_title
    private lateinit var viewModel: HostDetailsViewModel

    private val collapsingToolbarLayout by lazy { collapse_toolbar }

    private val appBarLayout by lazy { app_bar_layout }

    private val hostImageView by lazy { image_view_host }

    private val textAboutMe by lazy { text_about_me }

    private val textAddress by lazy { text_address }

    private val textPhone by lazy { text_phone }

    private val textPreferencePetSize by lazy { text_size }

    private val price by lazy { text_money_value }

    private val btnBook by lazy { btn_book }

    private val recyclerView by lazy { recycler_view }

    private lateinit var petAdapter: PetAdapter

    private lateinit var hostDTO: HostDTO

    private val cardBook by lazy { card_book }

    private val cardBookDetails by lazy { card_book_details }

    private val btnBookDetails by lazy { btn_book_details }

    override fun onPrepareActivity(state: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(HostDetailsViewModel::class.java)
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.gray_600))
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.gray_100))

        hostDTO = intent.getSerializableExtra(Data.HOST_DTO) as HostDTO
        viewModel.initViewModel(hostDTO)
        setHostImage(hostDTO.profileDTO.profileImageURL)

        setFields(hostDTO)

        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_gray_24dp)
                    collapsingToolbarLayout.title = getString(activityTitle)
                    isShow = true
                } else if (isShow) {
                    if (hostDTO.profileDTO.profileImageURL.isEmpty()) {
                        collapsingToolbarLayout.setExpandedTitleColor(
                            ContextCompat
                                .getColor(this@HostDetailsActivity, R.color.gray_600)
                        )
                        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_gray_24dp)
                    } else {
                        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
                    }
                    collapsingToolbarLayout.title = hostDTO.profileDTO.userName
                    isShow = false
                }
            }
        })

        if (hostDTO.petDTO.isEmpty()) card_my_pets.isVisible = false
        else {
            petAdapter = PetAdapter(this, hostDTO.petDTO.toMutableList(), this)
            recyclerView.adapter = petAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        btnBook.setSafeOnClickListener { startBookActivity() }
        btnBookDetails.setSafeOnClickListener { startBookDetailsActivity() }
        refreshData()
    }

    private fun startBookActivity() {
        if (!SessionManager.isLoggedIn()) {
            startActivityForResult(Intent(this, LoginActivity::class.java), RequestCode.SIGN_UP)
        } else {
            val intent = Intent(this, BookActivity::class.java).apply {
                putExtra(Data.HOST_DTO, hostDTO)
            }
            startActivityForResult(intent, RequestCode.REFRESH_HOST_DETAILS)
        }
    }

    private fun startBookDetailsActivity() {
        val intent = Intent(this, BookDetailsActivity::class.java).apply {
            putExtra(Data.BOOK_DETAILS_DTO, viewModel.getBookDetails())
        }
        startActivityForResult(intent, RequestCode.REFRESH_HOST_DETAILS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.SIGN_UP -> startBookActivity()
                RequestCode.REFRESH_HOST_DETAILS -> refreshData()
            }
        }
    }

    private fun refreshData() {
        viewModel.getHost()
        viewModel.host.observe(this, Observer {
            setFields(it)
        })
    }

    private fun setFields(host: HostDTO) {
        textAboutMe.text = host.about

        val address = "${host.profileDTO.addressDTO.street}, ${host.profileDTO.addressDTO.number}\n" +
                "${host.profileDTO.addressDTO.district}\n" +
                "${host.profileDTO.addressDTO.city} - ${host.profileDTO.addressDTO.state}"
        textAddress.text = address

        var phone = ""
        phone += host.profileDTO.telephone + "\n" + host.profileDTO.cellphone

        textPhone.text = phone

        var petSize = ""
        host.sizePreferenceList.forEach { size ->
            when (size) {
                PetSizeEnum.SMALL.name -> {
                    petSize += "${getString(PetSizeEnum.SMALL.description)}, "
                }
                PetSizeEnum.MEDIUM.name -> {
                    petSize += "${getString(PetSizeEnum.MEDIUM.description)}, "
                }
                PetSizeEnum.LARGE.name -> {
                    petSize += "${getString(PetSizeEnum.LARGE.description)}, "
                }
            }
        }
        textPreferencePetSize.text = petSize.removeSuffix(", ")

        price.text = host.price

        cardBookDetailsVisibility(host.bookingDetailsDTO != null)
    }

    private fun cardBookDetailsVisibility(visibility: Boolean) {
        cardBookDetails.isVisible = visibility
        cardBook.isVisible = hostDTO.profileDTO.id != SessionManager.getUserId().toString() && !visibility
    }

    private fun setHostImage(imageURL: String) {
        val progressBar = CircularProgressBar(this)
        progressBar.start()

        Glide.with(this)
            .load(imageURL)
            .error(R.drawable.ic_account_circle)
            .placeholder(progressBar)
            .transition(DrawableTransitionOptions.withCrossFade(Time.IMAGE_FADE))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(hostImageView)
    }

    /**
     *  Ação do botão de voltar da actionBar
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(pet: PetDTO) {
        val intent = Intent(this, PetDetailsActivity::class.java).apply {
            putExtra(Data.PET_DTO, pet)
        }
        startActivity(intent)
    }
}
