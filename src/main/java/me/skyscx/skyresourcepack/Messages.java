package me.skyscx.skyresourcepack;

import net.md_5.bungee.api.ChatColor;

public class Messages {
    public static String usageCMD = " Используй: §7/rp upload <name> <url>, load <name>, delete <name>, update <name> <newUrl>, reload, server";
    public static String cmdUpload = " Используй: §7/rp upload <name> <url>";
    public static String existingRP = "§c Пакет ресурсов с таким именем уже существует!";
    public String succefulUpload(String name, int id){
        return ChatColor.of("#008080") + " Пакет ресурсов §7" + name + ChatColor.of("#008080") +  " успешно загружен! §r(id: §7" + id + "§r)";
    }

    public static String cmdLoad = " Используй: §7/rp load <name>";
    public static String notRP = "§c Пакет ресурсов с таким идентификатором не существует!";
    public static String noConsoleCMD = "§c Данную команду можно выполнить только через клиент!";
    public String loadRP(String name, int id){
        return ChatColor.of("#008080") + " Пакет ресурсов §7" + name + ChatColor.of("#008080") +  " успешно установлен! §r(id: §7" + id + "§r)";
    }
    public static String loadServRP = ChatColor.of("#008080") + " Пакет ресурсов §7SERVER" + ChatColor.of("#008080") + " успешно загружен!";
    public static String cmdDelete = " Используй: §7/rp delete <name>";
    public static String noAuthor = "§c Вы не имеете права выполнить данное действие с этим пакетом ресурсов!";
    public static String failSaveCFG = "§c Ошибка сохранения конфигурационного файла: ";
    public String deleteRP(String name){
        return  ChatColor.of("#008080") + " Пакет ресурсов §7" + name + ChatColor.of("#008080") +  " успешно удален с сервера!";
    }
    public static String cmdUpdate = " Используй: §7/rp update <name> <newUrl>";
    public String updatedRP(String name, int id){
        return ChatColor.of("#008080") + " Ссылка на пакет ресурсов §7" + name + ChatColor.of("#008080") +  " обновлена! §r(id: §7" + id + "§r)";
    }
    public static String reloadedCFG = ChatColor.of("#008080") + " Конфигурация успешно перезагружена!";
    public static String noServerRP = "§7 Пакет ресурсов сервера не были заданы!";
    public static String unkownCMD = "§7 Недействительная команда! Используй /rp help для помощи.";
    public static String serverRPset = " Используй: §7/rp server set <url>";
    public static String succServerRPset = ChatColor.of("#008080") + " Пакеты ресурса для сервера успешно установлены!";
    public static String invalidURL = "§c URL указан неправильно!";
    public static String disabledRP = ChatColor.of("#008080") + " Пакеты ресурсов отключены!";
    public static String emptyList = "§7 Список пакетов ресурсов пуст!";
    public static String listRP = ChatColor.of("#008080") + " Список пакетов ресурсов: §r";
    public static String invalidID = "§c Неверный ID";
    public static String helpCMD = ChatColor.of("#008080") +" Список команд:\n" +
            "§7/rp server §r- Установить пакет ресурсов сервера.§r\n" +
            "§7/rp load <id> §r- Установить пакет ресурсов.§r\n" +
            "§7/rp disable §r- Отключить установленный пакет ресурсов.§r\n" +
            "§7/rp upload <name> <url> §r- Загрузить на сервер пакет ресурсов.§r\n" +
            "§7/rp update <name> <url> §r- Обновить ссылку на пакет ресурсов.§r\n" +
            "§7/rp list <player/my> <player> §r- Открыть список всех существующих пакетов ресурсов.§r\n" +
            "§7/rp delete <name> §r- Удалить загруженный пакет ресурсов.§r\n" +
            "§7/rp info <name/id> <name/id> §r- Получить информацию о пакете ресурсов.";
    public static String cmdInfo = " Используй: §7/rp info (name/id) <name/id>";
    public String info(String name, int id, int version, String owner){
        return ChatColor.of("#008080") + " Информация о пакете ресурсов §r" + name + ChatColor.of("#008080") + " :§r\n" +
                "   └ §7Идентификатор: §6" + id + "§r\n" +
                "   └ §7Владелец: §6" + owner + "§r\n" +
                "   └ §7Версия: §6" + version;
    }
    public static String moreUploadsError = "§7 Вы уже загрузили максимальное количество пакетов ресурсов!";
    public static String myRPlist = ChatColor.of("#008080") + " Ваши пакеты ресурсов: §r";
    public String rpList(String name){
        return ChatColor.of("#008080") + " Пакеты ресурсов " + name + ChatColor.of("#008080") + ": §r";
    }
    public static String emptyListRP = "§c Список пакетов ресурсов пуст!";
    public static String listPlayerCMD = " Используй: §7/rp list player <nickname>";
    public static String inSignNoRP = "§c Пакет ресурсов не найден или был удален!";
    public static String failLoadRP = "§c Пакет ресурсов настроен неправильно!";
    public static String failCooldownLoadRP = "§c Ошибка установки! Повторите попытку позже.\n" +
            "§7 Возможно, вы запретили установку RP сервера.";

}
