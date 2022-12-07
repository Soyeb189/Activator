package com.zaynaxhealth.activator.ui.home

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.zaynaxhealth.activator.R
import com.zaynaxhealth.activator.core.ApplicationClass
import com.zaynaxhealth.activator.data.model.EventActivatorInfo
import com.zaynaxhealth.activator.data.model.EventDrawer
import com.zaynaxhealth.activator.databinding.ActivityHomeBinding
import com.zaynaxhealth.activator.utilities.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController:NavController

    private var appUpdate : AppUpdateManager? = null
    private val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAppBarWithDrawer()
        destinationBinding()
        clickEvents()

        appUpdate = AppUpdateManagerFactory.create(this)
        checkUpdate()
        Log.d("UPDATE","updateProgress")

        //****** For Header ****//
        val header = binding.navView.getHeaderView(0)
        val tvName : TextView = header.findViewById(R.id.tvActivatorName)
        val tvType : TextView = header.findViewById(R.id.tvActivatorType)
        tvName.text = sharedPreference(ApplicationClass.AppContext!!).getString(ACTIVATOR_NAME)
        tvType.text = sharedPreference(ApplicationClass.AppContext!!).getString(USER_TYPE)
            ?.let { CamelCaseTitle.formatTitle(it) }
        //****** For Header ****//
    }

    private fun checkUpdate(){
        appUpdate?.appUpdateInfo?.addOnSuccessListener { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                appUpdate?.startUpdateFlowForResult(updateInfo,
                    AppUpdateType.IMMEDIATE,this,REQUEST_CODE)
            }
        }
    }

    private fun inProgressUpdate(){
        appUpdate?.appUpdateInfo?.addOnSuccessListener { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                appUpdate?.startUpdateFlowForResult(updateInfo,
                    AppUpdateType.IMMEDIATE,this,REQUEST_CODE)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        inProgressUpdate()
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun observeNameChange(data:EventActivatorInfo){
        val header = binding.navView.getHeaderView(0)
        val tvName : TextView = header.findViewById(R.id.tvActivatorName)
        tvName.text = data.name
        val tvType : TextView = header.findViewById(R.id.tvActivatorType)
        tvType.text = CamelCaseTitle.formatTitle(data.type)

    }



    private fun clickEvents() {
        binding.contentMain.ivDrawer.setOnClickListener { drawerOpenClose(null) }
        binding.contentMain.btnLogout.setOnClickListener { logout() }
        navDrawerMenuClick()
    }

    private fun setAppBarWithDrawer() {
        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHost.navController
    }

    private fun destinationBinding() {
        navController.addOnDestinationChangedListener{_, destination, _ ->
            when(destination.id){
                R.id.splashFragment,
                R.id.userInfoSubmitFragment,
                R.id.historyContainerFragment,
                R.id.successOrFailFragment,
                R.id.paymentGateWay,
                R.id.loginFragment ->{
                    binding.contentMain.actionBar.visibility = GONE
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }

                else ->{
                    binding.contentMain.actionBar.visibility = VISIBLE
//                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }
    }

    @Subscribe
    fun drawerOpenClose(eventDrawer: EventDrawer?){
        if (binding.drawerLayout.isOpen) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun logout(){
        sharedPreference(applicationContext).clearData()
        navController.navigate(R.id.loginFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    private fun navDrawerMenuClick(){
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navMenuUserNumberVerification ->{navController.navigate(R.id.userNumberVerificationFragment)}
                R.id.navMenuHistoryContainer ->{navController.navigate(R.id.historyContainerFragment)}
            }
            true
        }
    }
}