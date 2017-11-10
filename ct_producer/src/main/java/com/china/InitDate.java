package com.china;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitDate {

    //用于存放电话号码，准备随机产生数据使用
    private static List<String> phoneList = null;

    //存放电话号码与 联系人姓名的映射，置于Map集合中
    private static Map<String, String> contacts = null;

    public static List<String > getPhoneList(){

        phoneList = new ArrayList<>();

        phoneList.add("15307047580");
        phoneList.add("18870424309");
        phoneList.add("18879487552");
        phoneList.add("18870422690");
        phoneList.add("18870420329");
        phoneList.add("18870424059");
        phoneList.add("15946982826");
        phoneList.add("18870424829");
        phoneList.add("18870424679");
        phoneList.add("15779129551");
        phoneList.add("18870426792");
        phoneList.add("15779128269");
        phoneList.add("18870421232");
        phoneList.add("15350049306");
        phoneList.add("18870420286");
        phoneList.add("18870422715");
        phoneList.add("18870424012");
        phoneList.add("18379482674");
        phoneList.add("18870424151");
        phoneList.add("15727500684");
        phoneList.add("15870783646");
        phoneList.add("15079087108");
        phoneList.add("18870420591");
        phoneList.add("18870421557");
        phoneList.add("18870420253");
        phoneList.add("15779119361");
        phoneList.add("15070242997");
        phoneList.add("18870424092");
        phoneList.add("15779126872");
        phoneList.add("15970184647");
        phoneList.add("18870422771");
        phoneList.add("18179437645");
        phoneList.add("18870420619");
        phoneList.add("17770424094");
        phoneList.add("15779507668");
        phoneList.add("18779499052");
        phoneList.add("18870423412");
        phoneList.add("18870421309");
        phoneList.add("18797945412");
        phoneList.add("18870440104");
        phoneList.add("13767672348");
        phoneList.add("18870440164");
        phoneList.add("18870421533");
        phoneList.add("15679459173");
        phoneList.add("18870421218");
        phoneList.add("13367092006");
        phoneList.add("15932963862");
        phoneList.add("18870422601");
        phoneList.add("15870910492");
        phoneList.add("18007940441");
        phoneList.add("18870421157");
        phoneList.add("18707949366");
        phoneList.add("18870420362");
        phoneList.add("18870420702");
        phoneList.add("13767230858");
        phoneList.add("18870421507");
        phoneList.add("15707945187");
        phoneList.add("15079124871");
        phoneList.add("15879841006");
        phoneList.add("18870424020");
        phoneList.add("18870420523");
        phoneList.add("18870414873");
        phoneList.add("13134035127");
        phoneList.add("18870421195");

        return phoneList;

    }

    public static Map<String,String> getContacts() {

        contacts = new HashMap<>();

        contacts.put("15307047580", "彭梓雄");
        contacts.put("18870424309", "谌祎杰");
        contacts.put("18879487552", "肖浩东");
        contacts.put("18870422690", "洪莉");
        contacts.put("18870420329", "张卿顺");
        contacts.put("18870424059", "刘政");
        contacts.put("15946982826", "黄文涛");
        contacts.put("18870424829", "雷乔");
        contacts.put("18870424679", "龚永明");
        contacts.put("15779129551", "陈英华");
        contacts.put("18870426792", "黄旭");
        contacts.put("15779128269", "丁帆");
        contacts.put("18870421232", "杨艺");
        contacts.put("15350049306", "杨建锋");
        contacts.put("18870420286", "肖鹏");
        contacts.put("18870422715", "赵庆");
        contacts.put("18870424012", "刘丽华");
        contacts.put("18379482674", "余凡");
        contacts.put("18870424151", "刘秀秀");
        contacts.put("15727500684", "张小冬");
        contacts.put("15870783646", "高朝红");
        contacts.put("15079087108", "陶海洋");
        contacts.put("18870420591", "廖深海");
        contacts.put("18870421557", "周雅庆");
        contacts.put("18870420253", "谢庭");
        contacts.put("15779119361", "陈明珠");
        contacts.put("15070242997", "刘琪");
        contacts.put("18870424092", "程鹏");
        contacts.put("15779126872", "麻路璐");
        contacts.put("15970184647", "赖搏宇");
        contacts.put("18870422771", "张文彬");
        contacts.put("18179437645", "何诚");
        contacts.put("18870420619", "谢伟华");
        contacts.put("17770424094", "吴祖乐");
        contacts.put("15779507668", "杨小斌");
        contacts.put("18779499052", "李强");
        contacts.put("18870423412", "刘波");
        contacts.put("18870421309", "蔡轩");
        contacts.put("18797945412", "孙志雨");
        contacts.put("18870440104", "刘启");
        contacts.put("13767672348", "曾寒毓");
        contacts.put("18870440164", "崔明亮");
        contacts.put("18870421533", "罗钦建");
        contacts.put("15679459173", "陈龙");
        contacts.put("18870421218", "涂堃");
        contacts.put("13367092006", "刘杰文");
        contacts.put("15932963862", "林邦华");
        contacts.put("18870422601", "袁嫣婷");
        contacts.put("15870910492", "夏聪");
        contacts.put("18007940441", "张剑锋");
        contacts.put("18870421157", "胡逸轩");
        contacts.put("18707949366", "金旭");
        contacts.put("18870420362", "余冰旎");
        contacts.put("18870420702", "马超");
        contacts.put("13767230858", "李懿");
        contacts.put("18870421507", "朱琳");
        contacts.put("15707945187", "吴志茂");
        contacts.put("15079124871", "熊茁雯");
        contacts.put("15879841006", "李子豪");
        contacts.put("18870424020", "苏国剑");
        contacts.put("18870420523", "刘春辉");
        contacts.put("18870414873", "靳阳阳");
        contacts.put("13134035127", "陈炜楠");
        contacts.put("18870421195", "熊建邦");

        return  contacts;
    }
}
