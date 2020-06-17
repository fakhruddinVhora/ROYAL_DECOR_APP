package com.example.royal_decor.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.royal_decor.Adapters.FunctionalitiesAdapter
import com.example.royal_decor.R
import com.example.royal_decor.Utils.Constants
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    /*lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView*/
    lateinit var imageAnim: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        init()
        //setupToolbar()
        settingGridView()
        settingBackgroundImages()


    }

    private fun settingBackgroundImages() {


        val imageList = ArrayList<Int>()
        imageList.clear()
        imageList.add(R.drawable.img9)
        imageList.add(R.drawable.img2)
        imageList.add(R.drawable.img3)
        imageList.add(R.drawable.img4)
        imageList.add(R.drawable.img1)
        imageList.add(R.drawable.img2)
        imageList.add(R.drawable.img3)
        imageList.add(R.drawable.img4)
        imageList.add(R.drawable.img1)
        imageList.add(R.drawable.img2)
        imageList.add(R.drawable.img3)
        imageList.add(R.drawable.img4)

        val aniFadeIn: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fadein)
        val aniFadeOut: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fadeout)


        var i = 0
        object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {


                aniFadeOut.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}
                    override fun onAnimationRepeat(animation: Animation) {}
                    override fun onAnimationEnd(animation: Animation) {
                        imageAnim.setImageDrawable(resources.getDrawable(imageList.get(i)))
                        aniFadeIn.setAnimationListener(object : AnimationListener {
                            override fun onAnimationStart(animation: Animation) {}
                            override fun onAnimationRepeat(animation: Animation) {}
                            override fun onAnimationEnd(animation: Animation) {}
                        })
                        imageAnim.startAnimation(aniFadeIn)
                    }
                })
                imageAnim.startAnimation(aniFadeOut)





             /*   imageAnim.startAnimation(aniFadeOut)
                imageAnim.setImageDrawable(resources.getDrawable(imageList.get(i)))
                imageAnim.startAnimation(aniFadeIn)*/
                i++
                if (i == imageList.size - 1) {
                    i = 0
                }
                start()
            }
        }.start()




    }

    private fun settingGridView() {

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        width = metrics.widthPixels
        height = metrics.heightPixels

        val gridviewAdapter = FunctionalitiesAdapter()
        grid_view.adapter = gridviewAdapter
        grid_view.isVerticalScrollBarEnabled = false

        val List = getFunctionalityList()
        grid_view.setOnTouchListener { _, event ->
            event.action == MotionEvent.ACTION_MOVE
        }
        grid_view.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                val user = List[position].first
                val intent = Intent(applicationContext,DeskBaseActivity::class.java)
                intent.putExtra("ItemSelected",user)
                startActivity(intent)

            }
    }

    companion object {
        var height: Int = 0
        var width: Int = 0
    }

    private fun init() {

        imageAnim = findViewById(R.id.slide_trans_imageswitcher)

    }

    /*private fun setupToolbar() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }

    private fun init() {
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_newsurvey -> {

            }
            R.id.nav_logout -> {

            }
            R.id.nav_viewsurveys -> {

            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }*/

    fun getFunctionalityList(): List<Pair<String, Int>> {
        return listOf(
            Pair(Constants.ADD_PAINTER, Color.parseColor("#CD5C5C")),
            Pair(Constants.ADD_PRODUCT, Color.parseColor("#F08080")),
            Pair(Constants.EVALUATE_CREDITS, Color.parseColor("#FA8072")),
            Pair(Constants.UPDATE_DATA, Color.parseColor("#E9967A")),
            Pair(Constants.VIEW_CREDIT_SCORE, Color.parseColor("#FA8072")),
            Pair(Constants.VIEW_PAINTERS_LIST, Color.parseColor("#E9967A"))
        )
    }
}
