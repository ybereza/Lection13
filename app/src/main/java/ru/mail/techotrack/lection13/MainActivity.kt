package ru.mail.techotrack.lection13

import android.animation.TimeInterpolator
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        val mViewPager = findViewById<View>(R.id.container) as ViewPager
        mViewPager.adapter = mSectionsPagerAdapter
        // TODO: 12/05/16 Для показа
        //mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        //mViewPager.setPageTransformer(true, new DepthPageTransformer());
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    class ZoomOutPageTransformer : ViewPager.PageTransformer {

        companion object {
            private val MIN_SCALE = 0.85f
            private val MIN_ALPHA = 0.5f
        }

        override fun transformPage(view: View, position: Float) {
            val pageWidth = view.width
            val pageHeight = view.height

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.alpha = 0f

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                val vertMargin = pageHeight * (1 - scaleFactor) / 2
                val horzMargin = pageWidth * (1 - scaleFactor) / 2
                if (position < 0) {
                    view.translationX = horzMargin - vertMargin / 2
                } else {
                    view.translationX = -horzMargin + vertMargin / 2
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor

                // Fade the page relative to its size.
                view.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) *
                        (1 - MIN_ALPHA)

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.alpha = 0f
            }
        }

    }


    class DepthPageTransformer : ViewPager.PageTransformer {

        companion object {
            private val MIN_SCALE = 0.75f
        }

        override fun transformPage(view: View, position: Float) {
            val pageWidth = view.width

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.alpha = 0f

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.alpha = 1f
                view.translationX = 0f
                view.scaleX = 1f
                view.scaleY = 1f

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.alpha = 1 - position

                // Counteract the default slide transition
                view.translationX = pageWidth * -position

                // Scale the page down (between MIN_SCALE and 1)
                val scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position))
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.alpha = 0f
            }
        }

    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> return ValueAnimationFragment()
                1 -> return ObjectAnimationFragment()
                2 -> return ViewAnimationFragment()
                3 -> return ResAnimationFragment()
                4 -> return Res2AnimationFragment()
                5 -> return TransitionSimpleFragment()
                6 -> return TransitionFragment()
                7 -> return KeyFrameFragment()
                8 -> return WebViewFragment()
                9 -> return GifViewFragment()
                else -> return null
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 10
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "ValueAnimationFragment"
                1 -> return "ObjectAnimationFragment"
                2 -> return "ViewAnimationFragment"
                3 -> return "ResAnimationFragment"
                4 -> return "Res2AnimationFragment"
                5 -> return "TransitionSimpleFragment"
                6 -> return "TransitionFragment"
                7 -> return "KeyFrameFragment"
                8 -> return "WebViewFragment"
                9 -> return "GifViewFragment"
                else -> return ""
            }
        }
    }
}
