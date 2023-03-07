package com.example.leurope

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.leurope.databinding.MainMapaBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainMapa : Fragment(), OnMapReadyCallback {

    private lateinit var map:GoogleMap

    private var start:String=""
    private var end:String=""
    private var poly: Polyline? =null
    private lateinit var startLat:LatLng
    private lateinit var endLat:LatLng

    private lateinit var binding: MainMapaBinding
    private var Ies= LatLng(36.78180576899056, -2.815591899253087)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainMapaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        binding.fab.setOnClickListener {
            val cameraPosition = CameraPosition.Builder()
                .target(endLat) // Establece la posición de la cámara en Madrid
                .zoom(12f)      // Establece el nivel de zoom
                .build()
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
        binding.fabInicio.setOnClickListener {
            val cameraPosition = CameraPosition.Builder()
                .target(startLat) // Establece la posición de la cámara en Madrid
                .zoom(12f)      // Establece el nivel de zoom
                .build()
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.opciones, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.crear_Ruta->{
                Toast.makeText(requireContext(), "Clickea una vez para poner desde donde quieres ir y otra para poner a donde quieres llegar", Toast.LENGTH_LONG).show()
                start=""
                end=""
                if (::map.isInitialized){
                    map.setOnMapClickListener {
                        if (start.isEmpty()) {
                            start = "${it.longitude},${it.latitude}"
                            startLat= LatLng(it.latitude, it.longitude)
                        }else if (end.isEmpty()){
                            end = "${it.longitude},${it.latitude}"
                            endLat=LatLng(it.latitude, it.longitude)
                            createRoute()
                        }
                    }
                }
                true
            }

            R.id.Vichi->{
                start="-2.815591899253087,36.78180576899056"
                end="3.42082069374233,46.13039487016066"
                startLat= LatLng(36.78180576899056, -2.815591899253087)
                endLat=LatLng(46.13039487016066, 3.42082069374233)
                createRoute()
                true
            }

            R.id.Dublin->{
                start="-2.815591899253087,36.78180576899056"
                end="-6.265624430803462,53.338179601493316"
                startLat= LatLng(36.78180576899056, -2.815591899253087)
                endLat=LatLng(53.338179601493316, -6.265624430803462)
                createRoute()
                true
            }

            R.id.Ies->{
                start="-2.815591899253087,36.78180576899056"
                startLat= LatLng(36.78180576899056, -2.815591899253087)
                end=""
                if (::map.isInitialized){
                    map.setOnMapClickListener {
                        if (end.isEmpty()){
                            end = "${it.longitude},${it.latitude}"
                            endLat=LatLng(it.latitude, it.longitude)
                            createRoute()
                        }
                    }
                }
                true
            }
            else->true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map=map
        val cameraPosition = CameraPosition.Builder()
            .target(Ies) // Establece la posición de la cámara en Madrid
            .zoom(12f)      // Establece el nivel de zoom
            .build()
        this.map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun getRetrofit() : Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createRoute(){
        poly?.remove()
        binding.fabInicio.visibility=View.VISIBLE
        binding.fab.visibility=View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch{
            val call=getRetrofit().create(ApiService::class.java)
                .getRoute("5b3ce3597851110001cf62483c7f7f8f9583472fbe06640e3b774097", start, end)
            if (call.isSuccessful){
                drawRoute(call.body())
            }else{
                Log.i("aris", "KO")
            }
        }
    }

    private fun drawRoute(ruta: RouteResponse?) {
        val polylineOptions=PolylineOptions()
        ruta?.feau?.first()?.geo?.coordenadas?.forEach {
            polylineOptions.add(LatLng(it[1], it[0]))
        }
        requireActivity().runOnUiThread{
            poly=map.addPolyline(polylineOptions)
        }
    }
}