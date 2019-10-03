package app.phhuang.training

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        val navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)

        findViewById<NavigationView>(R.id.navigation).setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
