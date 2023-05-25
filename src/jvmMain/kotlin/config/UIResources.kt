package config

object UIResources {
    const val app_title = "Учет Рулонов"
    const val confirm_printing = "Отправить на печать"
    const val connection_on = "подключен"
    const val connection_off = "отключен"
    const val connection_status = "Статус сканера:"
    const val instructions_label = "Инструкции"
    const val total_processed="Всего обработано: "

    // Settings
    const val printer_ip_address="IP принтера"
    const val printer_port="Порт принтера"
    const val scanner_name="Наименование сканера"
    const val gtin_pattern="Шаблон GTIN'а"
    const val date_pattern="Шаблон даты"
    const val backend_url="URL сервера"
    const val settings_save_changes="Сохранить настройки"
    const val settings_set_defaults="Сбросить настройки"
    const val settings_saving_succeeded="Настройки успешно сохранены"
    const val settings_resetting_defaults="Дефолтные настройки успешно установлены"
    const val settings="settings"
    val instruction_set = listOf(
        "Убедитесь, что сканер подключен.",
        "Сканируйте код рулона.",
        "Ожидайте обработку кода.",
        "Подтвердите отправку этикетки на печать."
    )

}