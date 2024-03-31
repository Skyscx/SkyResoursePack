package me.skyscx.skyresourcepack;

import net.md_5.bungee.api.ChatColor;

public class Messages {
    public static String usageCMD = " Используй: §6/rp upload <name> <url>, load <name>, delete <name>, update <name> <newUrl>, reload, server";
    public static String cmdUpload = " Используй: §6/rp upload <name> <url>";
    public static String existingRP = "§c Пакет ресурсов с таким именем уже существует!";
    public String succefulUpload(String name, int id){
        return "§a Пакет ресурсов §6" + name + "§a успешно установлен! §r(id: §6" + id + "§r)";
    }

    public static String cmdLoad = " Используй: §6/rp load <name>";
    public static String notRP = "§c Пакет ресурсов с таким идентификатором не существует!";
    public static String noConsoleCMD = "§c Данную команду можно выполнить только через клиент!";
    public String loadRP(String name, int id){
        return "§a Пакет ресурсов §6" + name + "§a успешно загружен! §r(id: §6" + id + "§r)";
    }
    public static String loadServRP = "§a Пакет ресурсов §6SERVER§a успешно загружен!";
    public static String cmdDelete = " Используй: §6/rp delete <name>";
    public static String noAuthor = "§c Вы не имеете права выполнить данное действие с этим пакетом ресурсов!";
    public static String failSaveCFG = "§c Ошибка сохранения конфигурационного файла: ";
    public String deleteRP(String name){
        return "§a Пакет ресурсов §6" + name + "§a успешно удален с сервера!";
    }
    public static String cmdUpdate = " Используй: §6/rp update <name> <newUrl>";
    public String updatedRP(String name, int id){
        return " Ссылка на пакет ресурсов §6" + name + "§r обновлена! §r(id: §6" + id + "§r)";
    }
    public static String reloadedCFG = "§a Конфигурация успешно перезагружена!";
    public static String noServerRP = "§7 Пакет ресурсов сервера не были заданы!";
    public static String unkownCMD = "§7 Недействительная команда! Используй /rp help для помощи.";
    public static String serverRPset = " Используй: §6/rp server set <url>";
    public static String succServerRPset = "§a Пакеты ресурса для сервера успешно установлены!";
    public static String invalidURL = "§c URL указан неправильно!";
    public static String disabledRP = "§a Пакеты ресурсов отключены!";
    public static String emptyList = "§7 Список пакетов ресурсов пуст!";
    public static String listRP = ChatColor.of("#008080") + " Список пакетов ресурсов: §r";
    public static String invalidID = "§c Неверный ID";
    public static String helpCMD = ChatColor.of("#008080") +" Список команд:\n" +
            "§6/rp server §r- Установить пакет ресурсов сервера.§r\n" +
            "§6/rp load <id> §r- Установить пакет ресурсов.§r\n" +
            "§6/rp disable §r- Отключить установленный пакет ресурсов.§r\n" +
            "§6/rp upload <name> <url> §r- Загрузить на сервер пакет ресурсов.§r\n" +
            "§6/rp update <name> <url> §r- Обновить ссылку на пакет ресурсов.§r\n" +
            "§6/rp list <player/my> <player> §r- Открыть список всех существующих пакетов ресурсов.§r\n" +
            "§6/rp delete <name> §r- Удалить загруженный пакет ресурсов.§r\n" +
            "§6/rp info <name/id> <name/id> §r- Получить информацию о пакете ресурсов.";
    public static String cmdInfo = " Используй: §6/rp info (name/id) <name/id>";
    public String info(String name, int id, int version, String owner){
        return ChatColor.of("#008080") + " Информация о пакете ресурсов §r" + name + ChatColor.of("#008080") + " :§r\n" +
                "   └ §6Идентификатор: §7" + id + "§r\n" +
                "   └ §6Владелец: §7" + owner + "§r\n" +
                "   └ §6Версия: §7" + version;
    }
    public static String moreUploadsError = " Вы уже загрузили максимальное количество пакетов ресурсов!";
    public static String myRPlist = ChatColor.of("#008080") + " Ваши пакеты ресурсов: §r";
    public String rpList(String name){
        return ChatColor.of("#008080") + " Пакеты ресурсов " + name + ": §r";
    }
    public static String emptyListRP = "§c Список пакетов ресурсов пуст!";
}
