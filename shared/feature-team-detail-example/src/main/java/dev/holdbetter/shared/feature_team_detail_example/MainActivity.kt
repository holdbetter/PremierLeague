package dev.holdbetter.shared.feature_team_detail_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import dev.holdbetter.shared.feature_team_detail_impl.TeamDetailFragment
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            Napier.base(DebugAntilog())
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)

                val teamId = 2881L
                add(
                    R.id.root,
                    TeamDetailFragment.createFragment(teamId, ""),
                    TeamDetailFragment.tag
                )
            }
        }
    }
}