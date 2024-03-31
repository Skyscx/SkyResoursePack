package me.skyscx.skyresoursepack;

public class Messages {
    public static String usageCMD = "Используй: /rp upload <name> <url>, load <name>, delete <name>, update <name> <newUrl>, reload, server";
    public static String cmdUpload = "Используй: /rp upload <name> <url>";
    public static String existingRP = "Пакет ресурсов с таким именем уже существует!";
    public String succefulUpload(String name, int id){
        return "Пакет ресурсов «" + name + "» успешно загружен! (id: " + id + ")";
    }

    public static String cmdLoad = "Используй: /rp load <name>";
    public static String notRP = "Пакет ресурсов с таким именем не существует!";
    public static String noConsoleCMD = "Данную команду можно выполнить только через клиент!";
    public String loadRP(String name, int id){
        return "Пакет ресурсов «" + name + "» успешно загружен! (id: " + id + ")";
    }
    public static String loadServRP = "Пакет ресурсов «§6SERVER§r» успешно загружен!";
    public static String cmdDelete = "Используй: /rp delete <name>";
    public static String noAuthor = "Вы не имеете права выполнить данное действие с этим пакетом ресурсов!";
    public static String failSaveCFG = "Ошибка сохранения конфигурационного файла: ";
    public String deleteRP(String name){
        return "Пакет ресурсов «" + name + "» успешно удален с сервера!";
    }
    public static String cmdUpdate = "Используй: /rp update <name> <newUrl>";
    public String updatedRP(String name){
        return "Ссылка на пакет ресурсов «" + name + "» обновлена!";
    }
    public static String reloadedCFG = "Конфигурация успешно перезагружена!";
    public static String noServerRP = "Пакет ресурсов сервера не были заданы!";
    public static String unkownCMD = "Недействительная команда! Используй /rp для помощи.";
    public static String serverRPset = "Используй: /rp server set <url>";
    public static String succServerRPset = "Пакеты ресурса для сервера успешно установлены!";
    public static String invalidURL = "URL указан неправильно!";
    public static String disabledRP = "Пакеты ресурсов отключены!";
    public static String emptyList = "Список пакетов ресурсов пуст!";
    public static String listRP = "Список пакетов ресурсов:";
}
