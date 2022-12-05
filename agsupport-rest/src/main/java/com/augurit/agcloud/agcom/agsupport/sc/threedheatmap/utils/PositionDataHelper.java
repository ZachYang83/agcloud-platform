package com.augurit.agcloud.agcom.agsupport.sc.threedheatmap.utils;

import com.augurit.agcloud.agcom.agsupport.sc.threedheatmap.entity.RenderData;

import java.util.ArrayList;
import java.util.List;

public class PositionDataHelper
{

    /**
     * @param content 是某天某个时间点的所有xyw数据，格式为 x,y,w|x,y,w|...
     * @param cellSize 网格大小
     * @return
     */
    public static RenderData TransformData2(String content,int cellSize)
    {
        PointsInfo pointsInfo = GetPointsInfo(content);
        return GetRenderData(pointsInfo, cellSize);
    }

    public static RenderData GetRenderData(PointsInfo pointsInfo, double cellSize)
    {
        double std = 0.5;
        double maxWeight=0, minWeight=0;
        double searchRadius = cellSize * 8;
        double thresholdValue = 2.58 * std;
        List<Double> points = pointsInfo.points;
        int meshWidth = (int)Math.floor((pointsInfo.extent.maxX - pointsInfo.extent.minX) / cellSize) + 1;
        int meshHeight = (int)Math.floor((pointsInfo.extent.maxY - pointsInfo.extent.minY) / cellSize) + 1;
//        ArrayList<Double> boxps = new ArrayList<Double>(meshWidth * meshHeight * 3);
        Double[] boxps = new Double[meshWidth * meshHeight * 3];
        for (int i = 0; i < meshWidth; i++)
        {
            for (int j = 0; j < meshHeight; j++)
            {
                boxps[(meshWidth * j + i) * 3] = pointsInfo.extent.minX + cellSize / 2 + cellSize * i;
                boxps[(meshWidth * j + i) * 3 + 1] = pointsInfo.extent.minY + cellSize / 2 + cellSize * j;
                boxps[(meshWidth * j + i) * 3 + 2] = 0.0;
            }
        }

        for (int i = 0; i < points.size(); i += 3)
        {
            double x = points.get(i);
            double y = points.get(i + 1);
            double weight = points.get(i + 2);
            double r = Math.floor((y - pointsInfo.extent.minY) / cellSize) + 1;
            double c = Math.floor((x - pointsInfo.extent.minX) / cellSize) + 1;
            int index = (int)((r - 1) * meshWidth + c - 1) * 3;
            if (index > boxps.length - 2)
                continue;
            double x0 = boxps[index];
            double y0 = boxps[index + 1];
            double count = searchRadius / cellSize;

            for (double j = -count; j <= count; j++)
            {
                for (double k = -count; k <= count; k++)
                {
                    double r1 = r + j;
                    double c1 = c + k;
                    int pindex = (int)((r1 - 1) * meshWidth + c1 - 1) * 3;
                    if (pindex > boxps.length - 3 || pindex<0)
                        continue;
                    double px = boxps[pindex];
                    double py = boxps[pindex + 1];
                    double l = Math.pow(Math.abs(x - px), 2) + Math.pow(Math.abs(y - py), 2);
                    if (l <= Math.pow(searchRadius, 2))
                    {
                        double offsetX = ((px - x0) / searchRadius) * thresholdValue;
                        double offsetY = ((py - y0) / searchRadius) * thresholdValue;
                        double value = weight * Math.exp(-(Math.pow(offsetX, 2) / 2 / Math.pow(std, 2) + Math.pow(offsetY, 2) / 2 / Math.pow(std, 2)));
                        boxps[pindex + 2] = boxps[pindex + 2] + value;
                    }
                }
            }
        }
        for (int i = 0; i < boxps.length; i += 3)
        {
            double weight = boxps[i + 2];
            if (i == 0)
            {
                maxWeight = weight;
                minWeight = weight;
            }
            if (maxWeight < weight)
                maxWeight = weight;
            if (minWeight > weight)
                minWeight = weight;
        }
        for (int i = 0; i < boxps.length; i += 3)
        {
            double weight = boxps[i + 2];
            weight = pointsInfo.minWeight + ((weight - minWeight) / (maxWeight - minWeight)) * pointsInfo.maxWeight;
            boxps[i + 2] = weight; //计算密度，因为原始数据大概是10米一个点
        }

//        testCSV(boxps);//生成csv文件，可用于导入arcgis
        return new RenderData(cellSize, meshWidth , meshHeight, boxps );
    }

    /*private static void testCSV(Double[] points) {
        //生成CSV文件：
        String fileName = "2020-01-19.csv";
        try {
            FileOutputStream fos = new FileOutputStream("C:\\dev\\WorkSpaces\\qq_map\\csv\\" + fileName);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");

            CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("x", "y", "w");
            CSVPrinter csvPrinter = new CSVPrinter(osw, csvFormat);

//        csvPrinter = CSVFormat.DEFAULT.withHeader("姓名", "年龄", "家乡").print(osw);

            for (int i = 0; i < points.length; i=i+3 ) {
//                csvPrinter.printRecord(list.get(i), list.get(i+1), list.get(i+2));?
                csvPrinter.printRecord(points[i], points[i+1], points[i+2]);
            }

            csvPrinter.flush();
            csvPrinter.close();
        }catch (IOException e){
            System.out.println("创建csv失败！！");
            e.printStackTrace();
        }

    }*/


    public static PointsInfo GetPointsInfo(String content)
    {
        int indexX = 0;
        int indexY = 1;
        int indexW = 2;
        ArrayList<Double> points = new ArrayList<>();
        double pminX=0, pmaxX=0, pminY=0, pmaxY=0, pmaxWeight=0, pminWeight=0;
        String[] str = content.split("\\|");
        for (int i = 1; i < str.length; i++)
        {
            String[] temp = str[i].split(",");
            double x = Double.valueOf(temp[indexX]);
            double y = Double.valueOf(temp[indexY]);
            double weight = Double.valueOf(temp[indexW]);

            if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(weight))
                continue;
            if (i == 1)
            {
                pminX = x;
                pmaxX = x;
                pminY = y;
                pmaxY = y;
                pmaxWeight = weight;
                pminWeight = weight;
            }
            else
            {
                if (pminX > x)
                    pminX = x;
                if (pmaxX < x)
                    pmaxX = x;
                if (pminY > y)
                    pminY = y;
                if (pmaxY < y)
                    pmaxY = y;
                if (pmaxWeight < weight)
                    pmaxWeight = weight;
                if (pminWeight > weight)
                    pminWeight = weight;
            }
            points.add(x);
            points.add(y);
            points.add(weight);
        }
        Extent extent = new Extent(pminX, pmaxX, pminY, pmaxY);
        PointsInfo pointsInfo = new PointsInfo(points, pmaxWeight, pminWeight, extent );
        return pointsInfo;
    }
}

/*
class PositionData
{
    public String date;
    public List<Data> dataList;
}

class Data
{
    public String time;
    public List<RenderData> renderDataList;
}*/

class PointsInfo
{
    public List<Double> points;
    public double maxWeight;
    public double minWeight;
    public Extent extent;
    public PointsInfo(List<Double> points, double maxWeight, double minWeight, Extent extent){
        this.points = points;
        this.maxWeight = maxWeight;
        this.minWeight = minWeight;
        this.extent = extent;
    }
}

class Extent
{
    public double minX;
    public double maxX;
    public double minY;
    public double maxY;
    public Extent(double minx, double maxx, double miny, double maxy)
    {
        minX = minx;
        maxX = maxx;
        minY = miny;
        maxY = maxy;
    }
}



