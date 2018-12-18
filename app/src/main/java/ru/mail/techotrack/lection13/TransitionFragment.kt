package ru.mail.techotrack.lection13

import android.os.Bundle
import android.transition.Scene
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class TransitionFragment : ColorFragment() {

    private var bscene1 = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.transition_fragment, container, false)


        val sceneRoot = rootView.findViewById<View>(R.id.scene_root) as ViewGroup
        val scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.scene2, context)
        val scene1 = Scene.getSceneForLayout(sceneRoot, R.layout.scene1, context)

        val button = rootView.findViewById<View>(R.id.btn_change_scene) as Button

        button.setOnClickListener {
            TransitionManager.go(if (bscene1) scene2 else scene1)
            // TODO: 12/05/16 опишем свой аналог AutoTransition
            /*
				TransitionSet set = new TransitionSet();
				set.addTransition(new Fade());
				set.addTransition(new ChangeBounds());
				// выполняться они будут одновременно
				set.setOrdering(TransitionSet.ORDERING_TOGETHER);
				// уставим свою длительность анимации
				set.setDuration(5000);
				// и изменим Interpolator
				set.setInterpolator(new AccelerateInterpolator());
				TransitionManager.go(bscene1 ? scene2 : scene1, set);
				*/
            bscene1 = !bscene1
        }

        return rootView
    }
}
