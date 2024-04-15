package com.example.alquicar.view
import androidx.compose.material.Dialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint

@Composable
fun MapDialog(showDialog: MutableState<Boolean>, geoPoint: GeoPoint) {
    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            MapViewContainer(geoPoint)
        }
    }
}

@Composable
fun MapViewContainer(geoPoint: GeoPoint) {
    val mapView = rememberMapViewWithLifecycle()
    AndroidView({ mapView }) { mapView ->
        mapView.getMapAsync { googleMap ->
            val position = LatLng(geoPoint.latitude, geoPoint.longitude)
            googleMap.addMarker(MarkerOptions().position(position).title("Vehicle Location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
        }
    }
}
