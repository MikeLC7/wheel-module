package com.xcdh.target.util.idgen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangbo
 * @version 1.0 2017/12/20
 */
public class IpUtil {

    private static String macAddressStr = null;

    private static final String[] windowsCommand = {"ipconfig", "/all"};
    private static final String[] linuxCommand = {"/sbin/ifconfig", "-a"};

    private static final Pattern macPattern = Pattern.compile(".*((:?[0-9a-f]{2}[-:]){5}[0-9a-f]{2}).*", Pattern.CASE_INSENSITIVE);

    public static String getComputerId() {
        try {
            String mac = getMacAddress();
            String ip = getIpAddrAndName();
            if ((null == mac || mac.equals("")) && (null == ip || ip.equals("")))
                return null;

            if (null != mac && !mac.equals(""))
                return mac;

            return ip;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIpAddrAndName() throws IOException {
        return InetAddress.getLocalHost().toString();
    }

    public static String getMacAddress() {
        if (macAddressStr == null || macAddressStr.equals("")) {
            StringBuffer sb = new StringBuffer(); // 存放多个网卡地址用，目前只取一个非0000000000E0隧道的值
            try {
                List<String> macList = getMacAddressList();
                for (Iterator<String> iter = macList.iterator(); iter.hasNext(); ) {
                    String amac = iter.next();
                    if (!amac.equals("0000000000E0")) {
                        sb.append(amac);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            macAddressStr = sb.toString();
        }

        return macAddressStr;
    }

    private final static List<String> getMacAddressList() throws IOException {
        final ArrayList<String> macAddressList = new ArrayList<String>();
        final String os = System.getProperty("os.name");
        final String command[];

        if (os.startsWith("Windows")) {
            command = windowsCommand;
        } else if (os.startsWith("Linux")) {
            command = linuxCommand;
        } else {
            throw new IOException("Unknow operating system:" + os);
        }
        // 执行命令
        final Process process = Runtime.getRuntime().exec(command);

        BufferedReader bufReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        for (String line; (line = bufReader.readLine()) != null; ) {
            Matcher matcher = macPattern.matcher(line);
            if (matcher.matches()) {
                macAddressList.add(matcher.group(1));
            }
        }

        process.destroy();
        bufReader.close();
        return macAddressList;
    }
}
