package com.lightmock.mvpvmkotlin.version.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lightmock.core.binder.toast
import com.lightmock.mvpvmkotlin.R
import com.lightmock.mvpvmkotlin.databinding.FragmentVersionBinding
import com.lightmock.mvpvmkotlin.version.itf.IVersion
import com.lightmock.mvpvmkotlin.version.presenter.VersionPresenter
import com.lightmock.mvpvmkotlin.version.viewmodel.VersionViewModel
import kotlinx.android.synthetic.main.fragment_version.*

/**
 * Created by Llvve on 16/11/2017 AD.
 */
class VersionFragment: Fragment(), IVersion.IView, View.OnClickListener {

    private var binding: FragmentVersionBinding? = null
    private lateinit var viewModel: VersionViewModel
    private lateinit var presenter: VersionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(VersionViewModel::class.java)

        // Observe the viewmodel
        subscribeToViewModel()
    }

    /**
     * Subscribes to the VersionViewModel data.
     */
    private fun subscribeToViewModel() {
        // React to data changes on the version property

        presenter = VersionPresenter(this, viewModel)

        viewModel.getVersion().observe(this, Observer { version ->
            /**
             * Handle observe version data
             */

            // represent it!! by version
            // tv_result.text = "Version ".plus(it!!.device_name)
            binding!!.data = version
            tv_device.text = "Observe Device : ".plus(version!!.device_name) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_version, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Initiate api connection and action listener
         */
        btn_reload.setOnClickListener(this)
        rad_device_android.setOnClickListener(this)
        rad_device_ios.setOnClickListener(this)

        presenter.onViewInit()
    }

    override fun updateProgress(progress: String) {
        tv_progress.text = progress
    }

    /**
     * Emits when the user clicks the varies action
     */
    override fun onClick(view: View?) {
        when (view!!.id) {
            btn_reload.id -> {
                presenter.onReload()
            }
            rad_device_android.id -> {
                presenter.onDeviceSwitched(VersionViewModel.AppType.HANDIGO.value, VersionViewModel.Device.ANDROID.value)
            }
            rad_device_ios.id -> {
                presenter.onDeviceSwitched(VersionViewModel.AppType.HANDIGO.value, VersionViewModel.Device.IOS.value)
            }
            else -> {
                // TODO handle more case
            }
        }
    }

    /**
     * Emits when viewmodel already bind
     */
    override fun onBind(viewModel: VersionViewModel) {
        binding!!.viewModel = viewModel
    }

    override fun onFailureBinding(message: String, status: Int) {
        message.toast(context, Toast.LENGTH_LONG)
    }
}