package com.augurit.agcloud.agcom.agsupport.sc.backup;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: qinyg
 * @Date: 2020/11/10
 * @tips: 数据库备份controller，需要配置
 *  1、当前postgresql的安装目录的bin目录（postgresqlBinFullPath）
 *  2、需要配置的目标数据库名称（backupDatabaseName）
 */
@Api(value = "数据库备份", description = "数据库备份相关接口")
@RestController
@RequestMapping("/agsupport/configManager/db")
@ApiIgnore
public class AgPostgresqlBackUpController {

    @Value("${spring.datasource.username}")
    private String dbUserName;
    @Value("${spring.datasource.password}")
    private String dbPassword;
    @Value("${upload.filePath}")
    private String updateFilePath;

    private static final String postgresqlBinFullPath = "C:\\Program Files\\PostgreSQL\\10\\bin";
    private static final String backupDatabaseName = "agcim";

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/12 9:39
     * @tips: postgresql数据库备份，当前程序代码和当前安装的postgresql是同一台机器
     *
     */
    @GetMapping("/backcup")
    public ContentResultForm backcup() {
        try{
            //取系统时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhh");

            String backupDbName = backupDatabaseName;
            String datasourceUserName = dbUserName;
            String datasourcePassword = dbUserName;
            String pgSqlBinPath = postgresqlBinFullPath;
            String storeFileFullPathName = updateFilePath + sdf.format(date) + ".sql";

            //文件已存在，就不能进行备份
            File file = new File(storeFileFullPathName);
            if(file.exists()){
                return new ContentResultForm(false, "", "1个小时只能备份一次");
            }
            windowBackupPostgresql(backupDbName, datasourceUserName, datasourcePassword, pgSqlBinPath, storeFileFullPathName);
            return new ContentResultForm(true, "", "备份成功");
        }catch (Exception e){
            return new ContentResultForm(false, "", "备份失败");
        }
    }

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/12 9:39
     * @tips: postgresql数据库恢复备份，当前程序代码和当前安装的postgresql是同一台机器
     *
     */
    @GetMapping("/restore")
    public ContentResultForm restore(String fileName) {
        try{
            String backupDbName = backupDatabaseName;
            String datasourceUserName = dbUserName;
            String datasourcePassword = dbUserName;
            String pgSqlBinPath = postgresqlBinFullPath;
            String restoreFileFullPathName = updateFilePath + fileName;

            windowRestorePostgresql(backupDbName, datasourceUserName, datasourcePassword, pgSqlBinPath, restoreFileFullPathName);
            return new ContentResultForm(true, "", "恢复备份成功");
        }catch (Exception e){
            return new ContentResultForm(false, "", "恢复备份失败");
        }
    }


    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/10 14:58
     * @tips: window 版本，备份postgresql，
     * @param backupDbName 需要备份的数据库名称
     * @param datasourceUserName 数据库登录名称
     * @param datasourcePassword 数据库登录密码
     * @param pgSqlBinPath postgresql的安装路径，配置到bin的路径即可，例如： C:\Program Files\PostgreSQL\10\bin
     * @param storeFileFullPathName  备份文件存储路径和名称，如：E：\2020.sql
     * @return
     */
    public static void windowBackupPostgresql(String backupDbName, String datasourceUserName, String datasourcePassword, String pgSqlBinPath, String storeFileFullPathName) throws Exception {
        List<String> command = new ArrayList<>();
        command.add(pgSqlBinPath+"\\pg_dump");
        command.add("-c");
        command.add("-h");
        command.add("127.0.0.1");
        command.add("-U");
        command.add(datasourceUserName);
        command.add("-d");
        command.add(backupDbName);
        command.add("-f");
        command.add(storeFileFullPathName);

        ProcessBuilder pb = new ProcessBuilder(command).redirectErrorStream(true);
        Map<String,String> env = pb.environment();
        env.put("PGPASSWORD",datasourcePassword);
        Process process = pb.start();
    }


    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/10 15:08
     * @tips: window 版本，还原postgresql
     * @param  backupDbName 需要备份的数据库名称
     * @param  datasourceUserName 数据库登录名称
     * @param  datasourcePassword 数据库登录密码
     * @param  pgSqlBinPath postgresql的安装路径，配置到bin的路径即可，例如： C:\Program Files\PostgreSQL\10\bin
     * @param  restoreFileFullPathName  需要还原的文件存储路径和名称，如：E：\2020.sql
     * @return
     */
    public static void windowRestorePostgresql(String backupDbName, String datasourceUserName, String datasourcePassword, String pgSqlBinPath, String restoreFileFullPathName) throws Exception {
        List<String> command = new ArrayList<>();
        command.add(pgSqlBinPath+"\\psql");
        command.add("-h");
        command.add("127.0.0.1");
        command.add("-U");
        command.add(datasourceUserName);
        command.add("-d");
        command.add(backupDbName);
        command.add("-f");
        command.add(restoreFileFullPathName);

        ProcessBuilder pb = new ProcessBuilder(command).redirectErrorStream(true);
        Map<String,String> env = pb.environment();
        env.put("PGPASSWORD",datasourcePassword);
        Process process = pb.start();
    }

}
