import com.kevin.autolist.*;
import java.io.File;
import java.util.Map;

public class AutoList {

    private static final String XML_NAME_DEF_LAYOUT = "list_layout.xml";
    private static final String XML_NAME_PULL_LAYOUT = "list_pull_layout.xml";

    private static final String CODE_NAME_DECORATION = "ListItemDecoration";
    private static final String CODE_NAME_ADAPTER = "ListAdapter";
    private static final String CODE_NAME_LAYOUT = "ListLayout";

    public static void main(String[] args) {
        System.out.println("AutoList: 配置解析开始");
        ConfigBean.INSTANCE.parseConfig("./config.txt");
        System.out.println("AutoList: 配置解析完成");

        //if (readConfig(args)) {
            System.out.println("AutoList: 开始生成");
            Map<String, String> viewMap = new ItemXmlParser().parseElement(ConfigBean.INSTANCE.getItemXmlPath());
            String adaClzName = generateAdapterCode(viewMap);
            System.out.println("AutoList: Adapter代码已生成");
            String layResName = generateLayoutXml();
            System.out.println("AutoList: Layout布局已生成");
            String decClzName = generateDecorationCode();
            generateLayoutCode(adaClzName, layResName, decClzName);
            System.out.println("AutoList: Layout代码已生成");
        //}
    }

    private static boolean readConfig(String[] args) {
        String configPath = null;
        configPath = "./config.txt";
        /*if (args != null && args.length > 0) {
            configPath = args[0];
        }*/
        if (Utils.isStrNotEmpty(configPath)) {
            System.out.println("AutoList: 配置解析开始");
            ConfigBean.INSTANCE.parseConfig(configPath);
            System.out.println("AutoList: 配置解析完成");
            return true;
        } else {
            new TemplateWriter("/config.txt", "./config.txt").startWork();
            System.out.println("AutoList: 默认配置已导出");
            return false;
        }
    }

    private static String generateLayoutXml() {
        if (Utils.isStrNotEmpty(ConfigBean.INSTANCE.getLayoutXmlName())) {
            String originLayoutName = ConfigBean.INSTANCE.getHasPullRefresh() ? XML_NAME_PULL_LAYOUT : XML_NAME_DEF_LAYOUT;
            String targetLayoutName = ConfigBean.INSTANCE.getLayoutXmlName();
            new TemplateWriter(File.separator + originLayoutName,
                    "." + File.separator + targetLayoutName).startWork();
            return targetLayoutName.split("\\.")[0];
        } else {
            return "activity_main";
        }
    }

    private static String generateAdapterCode(Map<String, String> viewMap) {
        String originName = CODE_NAME_ADAPTER;
        String targetName;

        String bizName = ConfigBean.INSTANCE.getBizName();
        if (Utils.isStrNotEmpty(bizName)) {
            targetName = originName.replace("List", bizName);
        } else {
            targetName = originName;
        }
        WriteJoin replaceClzNameJoin = new SimpleReplaceWriteJoin(originName, targetName, "^c#", 0);

        String beanName = ConfigBean.INSTANCE.getDataBeanName();
        if (Utils.isStrEmpty(beanName)) {
            beanName = "String";
        }
        WriteJoin replaceBeamNameJoin = new SimpleReplaceWriteJoin("#P#", beanName, "^p#", 1);

        String itemXmlName = xmlFilePathToName(ConfigBean.INSTANCE.getItemXmlPath());
        AdapterInsertWriteJoin insertJoin  = new AdapterInsertWriteJoin(itemXmlName, viewMap, 2);

        String language = ConfigBean.INSTANCE.getLanguage();
        new TemplateWriter(File.separator + originName + language + ".tpe",
                "." + File.separator + targetName + language)
                .join(replaceClzNameJoin)
                .join(replaceBeamNameJoin)
                .join(insertJoin).startWork();
        return targetName;
    }

    private static String xmlFilePathToName(String itemXmlPath) {
        if (Utils.isStrNotEmpty(itemXmlPath)) {
            int end = itemXmlPath.length();
            int start = 0;
            if (itemXmlPath.endsWith(".xml")) {
                end = itemXmlPath.length() - 4;
            }
            int pathEndIndex = itemXmlPath.lastIndexOf(File.separator);
            if (pathEndIndex >= 0) {
                start = pathEndIndex + 1;
            }
            if (start < end) {
                return itemXmlPath.substring(start, end);
            }
        }
        return itemXmlPath;
    }

    private static String generateDecorationCode() {
        if (ConfigBean.INSTANCE.getHasItemDecoration()) {
            String originName = CODE_NAME_DECORATION;
            String bizName = ConfigBean.INSTANCE.getBizName();
            String targetName;
            if (Utils.isStrNotEmpty(bizName)) {
                targetName = originName.replace("List", bizName);
            } else {
                targetName = originName;
            }
            WriteJoin replaceJoin = new SimpleReplaceWriteJoin(originName, targetName, "^c#", 0);

            String language = ConfigBean.INSTANCE.getLanguage();
            new TemplateWriter(File.separator + originName + language + ".tpe",
                    "." + File.separator + targetName + language)
                    .join(replaceJoin).startWork();
            return targetName;
        }
        return CODE_NAME_DECORATION;
    }

    private static void generateLayoutCode(String adaClzName, String layResName, String decClzName) {
        String originName = CODE_NAME_LAYOUT;
        String targetName;
        String bizName = ConfigBean.INSTANCE.getBizName();
        if (Utils.isStrNotEmpty(bizName)) {
            targetName = originName.replace("List", bizName);
        } else {
            targetName = originName;
        }
        WriteJoin replaceClzNameJoin = new LayoutClzLineWriteJoin("^c#", originName, targetName);
        WriteJoin layoutInsertJoin = new LayoutInsertWriteJoin(decClzName, 1);
        WriteJoin adaReplaceJoin = new SimpleReplaceWriteJoin("#P#", adaClzName, "^p0#", 2);
        WriteJoin layReplaceJoin = new SimpleReplaceWriteJoin("#P#", layResName, "^p1#", 3);

        String language = ConfigBean.INSTANCE.getLanguage();
        new TemplateWriter(File.separator + originName + language + ".tpe",
                "." + File.separator + targetName + language)
                .join(replaceClzNameJoin)
                .join(layoutInsertJoin)
                .join(adaReplaceJoin)
                .join(layReplaceJoin).startWork();

    }

}
