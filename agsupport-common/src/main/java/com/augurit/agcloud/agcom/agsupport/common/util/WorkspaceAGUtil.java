package com.augurit.agcloud.agcom.agsupport.common.util;

import com.esri.arcgis.datasourcesGDB.SdeWorkspaceFactory;
import com.esri.arcgis.datasourcesfile.CadWorkspaceFactory;
import com.esri.arcgis.geodatabase.*;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.Cleaner;
import com.esri.arcgis.system.IPropertySet;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * workspace帮助类
 * 
 * @author cannel
 * 
 */
public class WorkspaceAGUtil {
	/**
	 * 获取CAD文件的workspace
	 * 
	 * @param filePath
	 * @param cadLayerType
	 *            图层的几何类型，只能是以下几个（注意大小写）：Annotation, MultiPatch, Point, Polygon,
	 *            Polyline
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public static IFeatureClass getCADFeatureClass(String filePath,
												   String cadLayerType) throws Exception, IOException {
		// File对象是为了获取各种文件路径格式
		File file = new File(filePath);

		IWorkspaceFactory workspaceFactoryCAD = new CadWorkspaceFactory();
		IFeatureWorkspace featureWorkspaceCAD = (IFeatureWorkspace) workspaceFactoryCAD
				.openFromFile(file.getParent(), 0);

		IFeatureClass featureClass = featureWorkspaceCAD.openFeatureClass(file
				.getName() + ":" + cadLayerType);

		return featureClass;
	}

	/**
	 * 获取SDE的Workspace（使用PropertySet）
	 * 
	 * @param propertySet
	 * @return
	 * @throws UnknownHostException
	 * @throws Exception
	 */
	public static IWorkspace getSDEWorkspaceByPropertySet(
			IPropertySet propertySet) throws UnknownHostException, Exception {
		IWorkspaceFactory workspaceFactory = new SdeWorkspaceFactory();
		IWorkspace workspace = workspaceFactory.open(propertySet, 0);

		return workspace;
	}

	/**
	 * 获取workspace内所有的featureclass
	 * 
	 * @param workspace
	 * @return
	 * @throws AutomationException
	 * @throws Exception
	 */
	public static List<IFeatureClass> getWorkspaceAllFeatureClass(
			IWorkspace workspace) throws AutomationException, Exception {
		// 返回的结果
		List<IFeatureClass> result = new ArrayList<IFeatureClass>();

		// 先获取所有的Dataset
		IEnumDataset enumDataset = workspace
				.getDatasets(esriDatasetType.esriDTFeatureDataset);
		enumDataset.reset();

		IFeatureWorkspace featureWorkspace = (IFeatureWorkspace) workspace;
		IDatasetProxy datasetProxy = null;

		int datasetCount = 0;

		while ((datasetProxy = (IDatasetProxy) enumDataset.next()) != null) {

			// 获取Dateset内的featureclass
			IEnumDataset enumFeatureClass = datasetProxy.getSubsets();
			enumFeatureClass.reset();

			IFeatureClass featureClass = null;
			IDataset datasetSub = null;

			while ((datasetSub = enumFeatureClass.next()) != null) {
				// 先获取featureclass的名称，再通过名称获取featureclass对象
				featureClass = featureWorkspace.openFeatureClass(datasetSub
						.getName());
				// 添加featureclass到结果集合
				result.add(featureClass);
			}

			// 手动释放对象
			Cleaner.release(enumFeatureClass);
			Cleaner.release(datasetProxy);
		}

		// 再遍历GDB根目录下的featureclass
		enumDataset = workspace.getDatasets(esriDatasetType.esriDTFeatureClass);
		enumDataset.reset();

		IDataset datasetSub = null;

		while ((datasetSub = enumDataset.next()) != null) {
			IFeatureClass featureClass = featureWorkspace
					.openFeatureClass(datasetSub.getName());
			// 添加featureclass到结果集合
			result.add(featureClass);
		}

		// 手动释放对象
		Cleaner.release(enumDataset);

		return result;
	}
}
