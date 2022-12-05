package com.augurit.agcloud.agcom.agsupport.common.util;

import com.common.util.ConfigProperties;
import com.esri.arcgis.system.AoInitialize;
import com.esri.arcgis.system.EngineInitializer;
import com.esri.arcgis.system.esriLicenseProductCode;
import com.esri.arcgis.system.esriLicenseStatus;
import com.esri.arcgis.version.VersionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Ao的初始化类
 *
 * @author cannel
 *
 */
@SuppressWarnings("deprecation")
public class AoInitUtil {
	private static Log logger = LogFactory.getLog(AoInitUtil.class);
	/**
	 * ao多线程锁 发现当多条线程同时使用ao时会造成异常，例如tomcat会自动关掉且没任何异常
	 * 为解决此问题，暂时的方法是使用线程锁，保证整个系统同一时间只有一条线程能使用ao
	 */
	public static Object aoLock = new Object();

	/**
	 * 初始化ao
	 *
	 * @param aoInit
	 * @return
	 */
	public AoInitialize initializeEngine(AoInitialize aoInit) {
		try {
			EngineInitializer.initializeEngine();

			// 设置使用的arcgis产品和版本，使ao能运行在不同的arcgis环境下
			// 下面这行代码会报错，可是VersionManager又要在AoInitialize实例化前运行，而且这个错不影响代码运行，所以暂时不管
			VersionManager versionManager = new VersionManager();
			// 第一个参数是arcgis产品编号：1=desktop，2=engine，5=server
			// 此参数可以通过枚举查看esriProductCode
			// versionManager.loadVersion(1, "10.0");
			versionManager.loadVersion(Integer.parseInt(ConfigProperties.getByKey("aoRuntimeProduct")), ConfigProperties.getByKey("aoRuntimeVersion"));

			aoInit = new AoInitialize();
			// 下面虽然有Engine和ArcInfo两种产品，但在本系统只有Engine才能用，ArcInfo会出错
			if (aoInit
					.isProductCodeAvailable(esriLicenseProductCode.esriLicenseProductCodeEngine) == esriLicenseStatus.esriLicenseAvailable)
				aoInit.initialize(esriLicenseProductCode.esriLicenseProductCodeEngine);
			else if (aoInit
					.isProductCodeAvailable(com.esri.arcgis.system.esriLicenseProductCode.esriLicenseProductCodeArcInfo) == com.esri.arcgis.system.esriLicenseStatus.esriLicenseAvailable)
				aoInit.initialize(com.esri.arcgis.system.esriLicenseProductCode.esriLicenseProductCodeArcInfo);

			// int licenseCode =
			// esriLicenseProductCode.esriLicenseProductCodeEngineGeoDB;
			// aoInit.initialize(licenseCode);

			return aoInit;
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error(e.getMessage(), e);
			return aoInit;
		}
	}
}
