package com.example.parentapp.view.appwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.parentapp.R
import com.example.parentapp.database.WeatherDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Implementation of App Widget functionality.
 */
class CustomAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val db = WeatherDatabase.getDatabase()
    val cityDao = db.cityDao()
    val weatherDao = db.weatherDao()

    GlobalScope.launch(Dispatchers.IO) {
        val city = cityDao.selectFirstCity()
        val weather = weatherDao.selectWeather(city.id)
        withContext(Dispatchers.Main){

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.custom_app_widget)
            views.setTextViewText(R.id.city_text, city.cityName)
            views.setTextViewText(R.id.weather_text, weather.minTemp + "/" + weather.maxTemp)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

}