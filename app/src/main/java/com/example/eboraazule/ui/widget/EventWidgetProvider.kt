package com.example.eboraazule.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.eboraazule.EboraApplication
import com.example.eboraazule.MainActivity
import com.example.eboraazule.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Actualizar todos los widgets activos
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int) {

        val views = RemoteViews(context.packageName, R.layout.event_widget_layout)

        // Al pulsar el widget, abrir la app
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_title, pendingIntent)

        // Obtener datos del repositorio usando el contenedor de la aplicación
        val repository = (context.applicationContext as EboraApplication).container.repository

        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.getEvents()
            val event = result.getOrNull()?.firstOrNull()

            if (event != null) {
                views.setTextViewText(R.id.widget_event_name, event.title)
                views.setTextViewText(R.id.widget_event_date, event.date)
            } else {
                views.setTextViewText(R.id.widget_event_name, "No hay eventos próximos")
            }
            
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
