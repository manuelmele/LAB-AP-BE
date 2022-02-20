package core.wefix.lab.utils.object;

public class Regex {
	public static final String onlyNumberRegex = "^[0-9]+$";
	public static final String licensePlateRegex = "^[A-Z]{2}+[0-9]{3}+[A-Z]{2}";
	public static final String latitudeRegex = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$";
	public static final String longitudeRegex = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$";
	public static final String functionalityRegex = "^(Car|HandicapCar|ElectricCar|Bus|Camper|Roulette|Motorcycle|LoadingAndUnloadingZone)$";
	public static final String colorRegex = "^(White|Blue|Yellow|Pink|Green)$";
	public static final String hourlyPriceRegex = "^(?:\\d{1,3}\\.\\d{1,2})$|^\\d{1,3}$";
	public static final String dailyPriceRegex = "^(?:\\d{1,3}\\.\\d{1,2})$|^\\d{1,3}$";
	public static final String weeklyPriceRegex = "^(?:\\d{1,4}\\.\\d{1,2})$|^\\d{1,4}$";
	public static final String monthlyPriceRegex = "^(?:\\d{1,4}\\.\\d{1,2})$|^\\d{1,4}$";
	public static final String lengthRegex = "^(?:\\d{1,2}\\.\\d{1,2})$|^\\d{1,2}$";
	public static final String widthRegex = "^(?:\\d{1,2}\\.\\d{1,2})$|^\\d{1,2}$";
	public static final String typeRegex = "^(Nastro|Pettine|Spina)$";
	public static final String parkingAreaStatusRegex = "^(Free|Busy|Damaged|Deleted)$";
	public static final String priceRegex = "^(?:\\d{1,10}\\.\\d{2,2})$";
	public static final String emailRegex = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,})$";
	public static final String passwordRegex = "^(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z])(.{8,30})$";
	public static final String bioRegex = "^^[A-Za-z\\d@!\\\"#$%&\\'()*+,-.\\/:;<=>?@^_`\\{|\\}~\\sÀ-ÿ\\[\\]]{0,128}$";
	public static final String categoryRegex = "^(Plumber|Painter|Electrician|Glazier|Gardener|Carpenter|ChimneySweep|Blacksmith)$";

	public static final String roleRegex = "^(User|Admin)$";
	public static final String firstNameRegex = "^^[a-zA-Z-]{0,20}$";
	public static final String secondNameRegex = "^^[a-zA-Z-]{0,20}$";
	public static final String newPassword = "newPassword";
	public static final String descriptionRegex = "^^[A-Za-z\\d@!\\\"#$%&\\'()*+,-.\\/:;<=>?@^_`\\{|\\}~\\sÀ-ÿ\\[\\]]{0,256}$";
	public static final String titleRegex = "^^[A-Za-z\\d@!\\\"#$%&\\'()*+,-.\\/:;<=>?@^_`\\{|\\}~\\sÀ-ÿ\\[\\]]{0,64}$";



}
