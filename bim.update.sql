/*
 Navicat Premium Data Transfer

 Source Server         : 81.71.142.155-pgsql
 Source Server Type    : PostgreSQL
 Source Server Version : 100012
 Source Host           : 81.71.142.155:5432
 Source Catalog        : agcim_bim
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 100012
 File Encoding         : 65001

 Date: 04/12/2020 10:07:20
*/


-- ----------------------------
-- Table structure for ag_house
-- ----------------------------
DROP TABLE IF EXISTS "public"."ag_house";
CREATE TABLE "public"."ag_house" (
  "id" varchar(72) COLLATE "pg_catalog"."default" NOT NULL,
  "source_name" varchar(255) COLLATE "pg_catalog"."default",
  "store_full_path" varchar(255) COLLATE "pg_catalog"."default",
  "suffix" varchar(72) COLLATE "pg_catalog"."default",
  "size" varchar(72) COLLATE "pg_catalog"."default",
  "type" varchar(72) COLLATE "pg_catalog"."default",
  "category_id" varchar(72) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "old_name" varchar(255) COLLATE "pg_catalog"."default",
  "hourse_name" varchar(255) COLLATE "pg_catalog"."default",
  "homestead_area" varchar(255) COLLATE "pg_catalog"."default",
  "floor_area" varchar(255) COLLATE "pg_catalog"."default",
  "covered_area" varchar(255) COLLATE "pg_catalog"."default",
  "cost_estimates" varchar(255) COLLATE "pg_catalog"."default",
  "is_show" varchar(10) COLLATE "pg_catalog"."default" DEFAULT 0,
  "source_id" varchar(72) COLLATE "pg_catalog"."default",
  "user_id" varchar(72) COLLATE "pg_catalog"."default",
  "thumb" text COLLATE "pg_catalog"."default",
  "table_name" varchar(255) COLLATE "pg_catalog"."default",
  "structure_type" varchar(255) COLLATE "pg_catalog"."default",
  "model_size" varchar(255) COLLATE "pg_catalog"."default",
  "status" int4,
  "component_code" varchar(255) COLLATE "pg_catalog"."default",
  "component_code_name" varchar(255) COLLATE "pg_catalog"."default",
  CONSTRAINT "ag_resource_copy1_pkey" PRIMARY KEY ("id")
)
;

COMMENT ON COLUMN "public"."ag_house"."id" IS '主键ID';
COMMENT ON COLUMN "public"."ag_house"."source_name" IS '源文件名称';
COMMENT ON COLUMN "public"."ag_house"."store_full_path" IS '文件存储路径';
COMMENT ON COLUMN "public"."ag_house"."suffix" IS '文件后缀';
COMMENT ON COLUMN "public"."ag_house"."size" IS '文件总大小';
COMMENT ON COLUMN "public"."ag_house"."type" IS '1模型图类型；2户型图类型；3缩略图';
COMMENT ON COLUMN "public"."ag_house"."category_id" IS '类别id';
COMMENT ON COLUMN "public"."ag_house"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_house"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_house"."remark" IS '备注';
COMMENT ON COLUMN "public"."ag_house"."old_name" IS '源文件名称';
COMMENT ON COLUMN "public"."ag_house"."hourse_name" IS '房屋名称';
COMMENT ON COLUMN "public"."ag_house"."homestead_area" IS '宅基地面积（平方米）';
COMMENT ON COLUMN "public"."ag_house"."floor_area" IS '占地面积（平方米）';
COMMENT ON COLUMN "public"."ag_house"."covered_area" IS '建筑面积（平方米）';
COMMENT ON COLUMN "public"."ag_house"."cost_estimates" IS '造价估计（平方米）';
COMMENT ON COLUMN "public"."ag_house"."is_show" IS '是否在列表展示（1展示，0不展示）';
COMMENT ON COLUMN "public"."ag_house"."source_id" IS '资源模型关联id（当前表）';
COMMENT ON COLUMN "public"."ag_house"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."ag_house"."thumb" IS '缩略图base64';
COMMENT ON COLUMN "public"."ag_house"."table_name" IS '模型关联属性表名';
COMMENT ON COLUMN "public"."ag_house"."structure_type" IS '结构类型';
COMMENT ON COLUMN "public"."ag_house"."model_size" IS '模型大小';
COMMENT ON COLUMN "public"."ag_house"."status" IS '1是rft；2是3dtiles';
COMMENT ON COLUMN "public"."ag_house"."component_code" IS '构件类目编码：表代码-大类.中类.小类.细类';
COMMENT ON COLUMN "public"."ag_house"."component_code_name" IS '构件类目编码名称：表代码-大类.中类.小类.细类';

-- ----------------------------
-- Table structure for ag_house_materials
-- ----------------------------
DROP TABLE IF EXISTS "public"."ag_house_materials";
CREATE TABLE "public"."ag_house_materials" (
  "id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "source_id" varchar(72) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."ag_house_materials"."id" IS '主键id';
COMMENT ON COLUMN "public"."ag_house_materials"."source_id" IS '来源id';
COMMENT ON COLUMN "public"."ag_house_materials"."name" IS '名称';
COMMENT ON COLUMN "public"."ag_house_materials"."url" IS '模型url';
COMMENT ON COLUMN "public"."ag_house_materials"."type" IS '类型（1消防栓、2树木、3自行车、4机动车）';
COMMENT ON COLUMN "public"."ag_house_materials"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_house_materials"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_house_materials"."remark" IS '备注';

-- ----------------------------
-- Table structure for ag_house_materials_point
-- ----------------------------
DROP TABLE IF EXISTS "public"."ag_house_materials_point";
CREATE TABLE "public"."ag_house_materials_point" (
  "id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "source_id" varchar(72) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "coordinates" varchar(255) COLLATE "pg_catalog"."default",
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "feature" text COLLATE "pg_catalog"."default",
  "floor" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."ag_house_materials_point"."id" IS '主键id';
COMMENT ON COLUMN "public"."ag_house_materials_point"."source_id" IS '来源id';
COMMENT ON COLUMN "public"."ag_house_materials_point"."name" IS '名称';
COMMENT ON COLUMN "public"."ag_house_materials_point"."coordinates" IS '坐标位置';
COMMENT ON COLUMN "public"."ag_house_materials_point"."url" IS '模型url';
COMMENT ON COLUMN "public"."ag_house_materials_point"."feature" IS '样式';
COMMENT ON COLUMN "public"."ag_house_materials_point"."floor" IS '楼层';
COMMENT ON COLUMN "public"."ag_house_materials_point"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_house_materials_point"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_house_materials_point"."remark" IS '备注';

-- ----------------------------
-- Table structure for ag_materials_component
-- ----------------------------
DROP TABLE IF EXISTS "public"."ag_materials_component";
CREATE TABLE "public"."ag_materials_component" (
  "id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "objectid" varchar(20) COLLATE "pg_catalog"."default",
  "name" varchar(200) COLLATE "pg_catalog"."default",
  "version" int8,
  "infotype" varchar(20) COLLATE "pg_catalog"."default",
  "profession" varchar(200) COLLATE "pg_catalog"."default",
  "level" varchar(200) COLLATE "pg_catalog"."default",
  "catagory" varchar(200) COLLATE "pg_catalog"."default",
  "familyname" varchar(200) COLLATE "pg_catalog"."default",
  "familytype" varchar(200) COLLATE "pg_catalog"."default",
  "materialid" varchar(100) COLLATE "pg_catalog"."default",
  "elementattributes" text COLLATE "pg_catalog"."default",
  "categorypath" varchar(500) COLLATE "pg_catalog"."default",
  "geometry" text COLLATE "pg_catalog"."default",
  "topologyelements" text COLLATE "pg_catalog"."default",
  "boundingbox" text COLLATE "pg_catalog"."default",
  "glb" bytea,
  "texture" varchar(255) COLLATE "pg_catalog"."default",
  "measure" varchar(255) COLLATE "pg_catalog"."default",
  "vendor" varchar(255) COLLATE "pg_catalog"."default",
  "single_price" varchar(255) COLLATE "pg_catalog"."default",
  "thumb" bytea,
  "table_name" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) DEFAULT now(),
  "modify_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "thumb_file_name" varchar(255) COLLATE "pg_catalog"."default",
  "glb_file_name" varchar(255) COLLATE "pg_catalog"."default",
  "status" int4,
  "model_size" varchar(255) COLLATE "pg_catalog"."default",
  "component_code" varchar(255) COLLATE "pg_catalog"."default",
  "component_code_name" varchar(255) COLLATE "pg_catalog"."default",
  "specification" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."ag_materials_component"."id" IS '主键ID';
COMMENT ON COLUMN "public"."ag_materials_component"."objectid" IS 'objectid';
COMMENT ON COLUMN "public"."ag_materials_component"."name" IS '构件名称';
COMMENT ON COLUMN "public"."ag_materials_component"."version" IS '版本';
COMMENT ON COLUMN "public"."ag_materials_component"."infotype" IS '类型： agRoom、agWall';
COMMENT ON COLUMN "public"."ag_materials_component"."profession" IS '专业；消防，电气，管道';
COMMENT ON COLUMN "public"."ag_materials_component"."level" IS '标高';
COMMENT ON COLUMN "public"."ag_materials_component"."catagory" IS '类别；墙；房间；楼层空间（虚拟）这个类型请多扩展；';
COMMENT ON COLUMN "public"."ag_materials_component"."familyname" IS '族名称';
COMMENT ON COLUMN "public"."ag_materials_component"."familytype" IS '族类型';
COMMENT ON COLUMN "public"."ag_materials_component"."materialid" IS '材料id';
COMMENT ON COLUMN "public"."ag_materials_component"."elementattributes" IS '用户填写的属性';
COMMENT ON COLUMN "public"."ag_materials_component"."categorypath" IS '类别路径';
COMMENT ON COLUMN "public"."ag_materials_component"."geometry" IS '几何信息包括纹理材质全部以gltf的数据格式进行存储，存储格式是glb';
COMMENT ON COLUMN "public"."ag_materials_component"."topologyelements" IS '构件：构件方向，尺寸标注信息（x、y、z方向最长的线段）；楼层：楼层高度；房间：房间内的构件编号、房间边界所组成的构件编号';
COMMENT ON COLUMN "public"."ag_materials_component"."boundingbox" IS '包围盒信息，如果为房间，还包含组成房间的所有边界';
COMMENT ON COLUMN "public"."ag_materials_component"."glb" IS 'glb构件';
COMMENT ON COLUMN "public"."ag_materials_component"."texture" IS '材质';
COMMENT ON COLUMN "public"."ag_materials_component"."measure" IS '尺寸（长x宽x高）';
COMMENT ON COLUMN "public"."ag_materials_component"."vendor" IS '厂商';
COMMENT ON COLUMN "public"."ag_materials_component"."single_price" IS '单价（元）';
COMMENT ON COLUMN "public"."ag_materials_component"."thumb" IS '缩略图';
COMMENT ON COLUMN "public"."ag_materials_component"."table_name" IS '材料关联属性表名';
COMMENT ON COLUMN "public"."ag_materials_component"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_materials_component"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_materials_component"."remark" IS '备注';
COMMENT ON COLUMN "public"."ag_materials_component"."thumb_file_name" IS '缩略图文件名';
COMMENT ON COLUMN "public"."ag_materials_component"."glb_file_name" IS 'glb文件名';
COMMENT ON COLUMN "public"."ag_materials_component"."status" IS '1是rfa；2是glb';
COMMENT ON COLUMN "public"."ag_materials_component"."model_size" IS '模型大小';
COMMENT ON COLUMN "public"."ag_materials_component"."component_code" IS '构件类目编码：表代码-大类.中类.小类.细类';
COMMENT ON COLUMN "public"."ag_materials_component"."component_code_name" IS '构件类目编码名称：表代码-大类.中类.小类.细类';
COMMENT ON COLUMN "public"."ag_materials_component"."specification" IS '长x宽x高';

-- ----------------------------
-- Table structure for ag_permission
-- ----------------------------
DROP TABLE IF EXISTS "public"."ag_permission";
CREATE TABLE "public"."ag_permission" (
  "id" varchar(72) COLLATE "pg_catalog"."default" NOT NULL,
  "source_id" varchar(72) COLLATE "pg_catalog"."default",
  "code" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."ag_permission"."source_id" IS '来源id';
COMMENT ON COLUMN "public"."ag_permission"."code" IS '数据字典的code值';
COMMENT ON COLUMN "public"."ag_permission"."type" IS '1用户；2构件；3房屋';

-- ----------------------------
-- Table structure for ag_sys_setting
-- ----------------------------
DROP TABLE IF EXISTS "public"."ag_sys_setting";
CREATE TABLE "public"."ag_sys_setting" (
  "id" varchar(72) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(72) COLLATE "pg_catalog"."default",
  "type" varchar(72) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(72) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Table structure for ag_user_design_materials
-- ----------------------------
DROP TABLE IF EXISTS "public"."ag_user_design_materials";
CREATE TABLE "public"."ag_user_design_materials" (
  "id" varchar(72) COLLATE "pg_catalog"."default" NOT NULL,
  "type" varchar(10) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "position" varchar(255) COLLATE "pg_catalog"."default",
  "orientation" varchar(255) COLLATE "pg_catalog"."default",
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "model_matrix" text COLLATE "pg_catalog"."default",
  "property_url" varchar(255) COLLATE "pg_catalog"."default",
  "user_id" varchar(72) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "design_scheme_id" varchar(72) COLLATE "pg_catalog"."default",
  "angle" varchar(255) COLLATE "pg_catalog"."default",
  "table_name" varchar(255) COLLATE "pg_catalog"."default",
  "components" text COLLATE "pg_catalog"."default",
  "style" text COLLATE "pg_catalog"."default",
  "tile_url" varchar(255) COLLATE "pg_catalog"."default",
  "component_type" varchar(255) COLLATE "pg_catalog"."default",
  "component_id" varchar(255) COLLATE "pg_catalog"."default",
  "boundingbox" text COLLATE "pg_catalog"."default",
  "topologyelements" text COLLATE "pg_catalog"."default",
  "obb_center" varchar(255) COLLATE "pg_catalog"."default",
  "subtract" varchar(255) COLLATE "pg_catalog"."default",
  "relation_ids" text COLLATE "pg_catalog"."default",
  "measure" varchar(255) COLLATE "pg_catalog"."default",
  "size" varchar(255) COLLATE "pg_catalog"."default",
  "clip_matrix" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."ag_user_design_materials"."type" IS '1房屋模型；2小品模型；3构件模型';
COMMENT ON COLUMN "public"."ag_user_design_materials"."name" IS '模型名称';
COMMENT ON COLUMN "public"."ag_user_design_materials"."position" IS '坐标';
COMMENT ON COLUMN "public"."ag_user_design_materials"."orientation" IS '方向';
COMMENT ON COLUMN "public"."ag_user_design_materials"."url" IS '模型地址';
COMMENT ON COLUMN "public"."ag_user_design_materials"."model_matrix" IS '模型矩阵';
COMMENT ON COLUMN "public"."ag_user_design_materials"."property_url" IS '属性地址';
COMMENT ON COLUMN "public"."ag_user_design_materials"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."ag_user_design_materials"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_user_design_materials"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_user_design_materials"."remark" IS '备注';
COMMENT ON COLUMN "public"."ag_user_design_materials"."design_scheme_id" IS 'ag_user_design_scheme的主键';
COMMENT ON COLUMN "public"."ag_user_design_materials"."angle" IS '角度';
COMMENT ON COLUMN "public"."ag_user_design_materials"."table_name" IS '房屋关联属性表名';
COMMENT ON COLUMN "public"."ag_user_design_materials"."components" IS '组件';
COMMENT ON COLUMN "public"."ag_user_design_materials"."style" IS '样式';
COMMENT ON COLUMN "public"."ag_user_design_materials"."tile_url" IS 'tile url';
COMMENT ON COLUMN "public"."ag_user_design_materials"."component_type" IS '组件类型';
COMMENT ON COLUMN "public"."ag_user_design_materials"."component_id" IS '构件id，前端参数';
COMMENT ON COLUMN "public"."ag_user_design_materials"."boundingbox" IS '包围盒';
COMMENT ON COLUMN "public"."ag_user_design_materials"."topologyelements" IS '拓扑关系';
COMMENT ON COLUMN "public"."ag_user_design_materials"."obb_center" IS 'obb包围盒中心';
COMMENT ON COLUMN "public"."ag_user_design_materials"."subtract" IS 'rvt模型包围盒中心与3dtiles模型包围盒中心差';
COMMENT ON COLUMN "public"."ag_user_design_materials"."relation_ids" IS '逗号分隔（自关联。房屋所关联的构件或者构件所在的房屋）';
COMMENT ON COLUMN "public"."ag_user_design_materials"."measure" IS '尺寸（长x宽x高）';
COMMENT ON COLUMN "public"."ag_user_design_materials"."size" IS '构件的尺寸,用来计算再次加载时剖切的大小';
COMMENT ON COLUMN "public"."ag_user_design_materials"."clip_matrix" IS '保存剖切的位置';

-- ----------------------------
-- Table structure for ag_user_design_scheme
-- ----------------------------
DROP TABLE IF EXISTS "public"."ag_user_design_scheme";
CREATE TABLE "public"."ag_user_design_scheme" (
  "id" varchar(72) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "land_info" varchar(255) COLLATE "pg_catalog"."default",
  "location" varchar(255) COLLATE "pg_catalog"."default",
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "thumb" varchar(255) COLLATE "pg_catalog"."default",
  "user_id" varchar(72) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "is_default" varchar(10) COLLATE "pg_catalog"."default" DEFAULT 0
)
;
COMMENT ON COLUMN "public"."ag_user_design_scheme"."name" IS '方案名称';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."land_info" IS '地块信息';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."location" IS '保存位置';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."description" IS '方案介绍';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."thumb" IS '预览图存储位置';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."is_default" IS '是否是默认（0否，1是）';

-- ----------------------------
-- Table structure for ag_user_message
-- ----------------------------
DROP TABLE IF EXISTS "public"."ag_user_message";
CREATE TABLE "public"."ag_user_message" (
  "id" varchar(72) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" varchar(72) COLLATE "pg_catalog"."default",
  "user_type" varchar(255) COLLATE "pg_catalog"."default",
  "organization" varchar(255) COLLATE "pg_catalog"."default",
  "security_question" varchar(255) COLLATE "pg_catalog"."default",
  "security_answer" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."ag_user_message"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."ag_user_message"."user_type" IS '用户类型';
COMMENT ON COLUMN "public"."ag_user_message"."organization" IS '机构';
COMMENT ON COLUMN "public"."ag_user_message"."security_question" IS '密保问题';
COMMENT ON COLUMN "public"."ag_user_message"."security_answer" IS '密保答案';
COMMENT ON COLUMN "public"."ag_user_message"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_user_message"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_user_message"."remark" IS '备注';

-- ----------------------------
-- Table structure for agcim3dentity_a
-- ----------------------------
DROP TABLE IF EXISTS "public"."agcim3dentity_a";
CREATE TABLE "public"."agcim3dentity_a" (
  "id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "objectid" varchar(20) COLLATE "pg_catalog"."default",
  "name" varchar(200) COLLATE "pg_catalog"."default",
  "version" int8,
  "infotype" varchar(20) COLLATE "pg_catalog"."default",
  "profession" varchar(200) COLLATE "pg_catalog"."default",
  "level" varchar(200) COLLATE "pg_catalog"."default",
  "catagory" varchar(200) COLLATE "pg_catalog"."default",
  "familyname" varchar(200) COLLATE "pg_catalog"."default",
  "familytype" varchar(200) COLLATE "pg_catalog"."default",
  "materialid" varchar(100) COLLATE "pg_catalog"."default",
  "elementattributes" text COLLATE "pg_catalog"."default",
  "categorypath" varchar(500) COLLATE "pg_catalog"."default",
  "geometry" text COLLATE "pg_catalog"."default",
  "topologyelements" text COLLATE "pg_catalog"."default",
  "boundingbox" text COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Table structure for agcim3dentity_vb1
-- ----------------------------
DROP TABLE IF EXISTS "public"."agcim3dentity_vb1";
CREATE TABLE "public"."agcim3dentity_vb1" (
  "id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "objectid" varchar(20) COLLATE "pg_catalog"."default",
  "name" varchar(200) COLLATE "pg_catalog"."default",
  "version" int8,
  "infotype" varchar(20) COLLATE "pg_catalog"."default",
  "profession" varchar(200) COLLATE "pg_catalog"."default",
  "level" varchar(200) COLLATE "pg_catalog"."default",
  "catagory" varchar(200) COLLATE "pg_catalog"."default",
  "familyname" varchar(200) COLLATE "pg_catalog"."default",
  "familytype" varchar(200) COLLATE "pg_catalog"."default",
  "materialid" varchar(100) COLLATE "pg_catalog"."default",
  "elementattributes" text COLLATE "pg_catalog"."default",
  "host" varchar(500) COLLATE "pg_catalog"."default",
  "related" varchar(500) COLLATE "pg_catalog"."default",
  "geometry" text COLLATE "pg_catalog"."default",
  "topologyelements" text COLLATE "pg_catalog"."default",
  "boundingbox" text COLLATE "pg_catalog"."default",
  "glb" bytea
)
;

-- ----------------------------
-- Table structure for agcim3dentity_vb2
-- ----------------------------
DROP TABLE IF EXISTS "public"."agcim3dentity_vb2";
CREATE TABLE "public"."agcim3dentity_vb2" (
  "id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "objectid" varchar(20) COLLATE "pg_catalog"."default",
  "name" varchar(200) COLLATE "pg_catalog"."default",
  "version" int8,
  "infotype" varchar(20) COLLATE "pg_catalog"."default",
  "profession" varchar(200) COLLATE "pg_catalog"."default",
  "level" varchar(200) COLLATE "pg_catalog"."default",
  "catagory" varchar(200) COLLATE "pg_catalog"."default",
  "familyname" varchar(200) COLLATE "pg_catalog"."default",
  "familytype" varchar(200) COLLATE "pg_catalog"."default",
  "materialid" varchar(100) COLLATE "pg_catalog"."default",
  "elementattributes" text COLLATE "pg_catalog"."default",
  "host" varchar(500) COLLATE "pg_catalog"."default",
  "related" varchar(500) COLLATE "pg_catalog"."default",
  "geometry" text COLLATE "pg_catalog"."default",
  "topologyelements" text COLLATE "pg_catalog"."default",
  "boundingbox" text COLLATE "pg_catalog"."default",
  "glb" bytea
)
;

-- ----------------------------
-- Table structure for agcim3dentity_vb3
-- ----------------------------
DROP TABLE IF EXISTS "public"."agcim3dentity_vb3";
CREATE TABLE "public"."agcim3dentity_vb3" (
  "id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
  "objectid" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "name" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "version" int8,
  "infotype" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "profession" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "level" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "catagory" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "familyname" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "familytype" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "materialid" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "elementattributes" text COLLATE "pg_catalog"."default",
  "geometry" text COLLATE "pg_catalog"."default",
  "topologyelements" text COLLATE "pg_catalog"."default",
  "boundingbox" text COLLATE "pg_catalog"."default",
  "host" varchar(500) COLLATE "pg_catalog"."default",
  "related" varchar(500) COLLATE "pg_catalog"."default",
  "glb" bytea
)
;

-- ----------------------------
-- Table structure for agcim3dentity_vb4
-- ----------------------------
DROP TABLE IF EXISTS "public"."agcim3dentity_vb4";
CREATE TABLE "public"."agcim3dentity_vb4" (
  "id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
  "objectid" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "name" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "version" int8,
  "infotype" varchar(20) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "profession" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "level" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "catagory" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "familyname" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "familytype" varchar(200) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "materialid" varchar(100) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "elementattributes" text COLLATE "pg_catalog"."default",
  "geometry" text COLLATE "pg_catalog"."default",
  "topologyelements" text COLLATE "pg_catalog"."default",
  "boundingbox" text COLLATE "pg_catalog"."default",
  "host" varchar(500) COLLATE "pg_catalog"."default",
  "related" varchar(500) COLLATE "pg_catalog"."default",
  "glb" bytea
)
;

-- ----------------------------
-- Table structure for agcim3dentity_via
-- ----------------------------
DROP TABLE IF EXISTS "public"."agcim3dentity_via";
CREATE TABLE "public"."agcim3dentity_via" (
  "id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "objectid" varchar(20) COLLATE "pg_catalog"."default",
  "name" varchar(200) COLLATE "pg_catalog"."default",
  "version" int8,
  "infotype" varchar(20) COLLATE "pg_catalog"."default",
  "catagory" varchar(200) COLLATE "pg_catalog"."default",
  "profession" varchar(200) COLLATE "pg_catalog"."default",
  "level" varchar(200) COLLATE "pg_catalog"."default",
  "materialid" varchar(100) COLLATE "pg_catalog"."default",
  "elementattributes" text COLLATE "pg_catalog"."default",
  "categorypath" varchar(500) COLLATE "pg_catalog"."default",
  "geometry" text COLLATE "pg_catalog"."default",
  "topologyelements" text COLLATE "pg_catalog"."default",
  "boundingbox" text COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Table structure for agcim3dproject  (更新20201210)
-- ----------------------------
DROP TABLE IF EXISTS "public"."agcim3dproject";
CREATE TABLE "public"."agcim3dproject" (
  "id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "units" varchar(255) COLLATE "pg_catalog"."default",
  "address" varchar(255) COLLATE "pg_catalog"."default",
  "owner" varchar(255) COLLATE "pg_catalog"."default",
  "creattime" varchar(100) COLLATE "pg_catalog"."default",
  "projectphase" varchar(100) COLLATE "pg_catalog"."default",
  "constructiontype" varchar(100) COLLATE "pg_catalog"."default",
  "ecoindex" varchar(100) COLLATE "pg_catalog"."default",
  "ecoindextable" varchar(100) COLLATE "pg_catalog"."default",
  "metadata" text COLLATE "pg_catalog"."default",
  "serverurl" varchar(200) COLLATE "pg_catalog"."default",
  "servertype" varchar(100) COLLATE "pg_catalog"."default",
  CONSTRAINT "agcim3dproject_pkey" PRIMARY KEY ("id")
)
;
ALTER TABLE "public"."agcim3dproject"
  OWNER TO "postgres";

COMMENT ON COLUMN "public"."agcim3dproject"."id" IS '项目ID';
COMMENT ON COLUMN "public"."agcim3dproject"."name" IS '工程名称';
COMMENT ON COLUMN "public"."agcim3dproject"."units" IS '工程所用的单位';
COMMENT ON COLUMN "public"."agcim3dproject"."address" IS '项目所在地址，地名';
COMMENT ON COLUMN "public"."agcim3dproject"."owner" IS '行政主体信息（JSON）';
COMMENT ON COLUMN "public"."agcim3dproject"."creattime" IS '项目的创建时间';
COMMENT ON COLUMN "public"."agcim3dproject"."projectphase" IS '工程阶段';
COMMENT ON COLUMN "public"."agcim3dproject"."constructiontype" IS '建筑类型:建筑;市政;桥梁;';
COMMENT ON COLUMN "public"."agcim3dproject"."ecoindex" IS '经济技术指标';
COMMENT ON COLUMN "public"."agcim3dproject"."ecoindextable" IS '经济技术指标表的表名（如有）';
COMMENT ON COLUMN "public"."agcim3dproject"."metadata" IS '包括创建者，工程人等不常用的信息。JSON字符串';
COMMENT ON COLUMN "public"."agcim3dproject"."serverurl" IS '服务地址（i3s:3dtiles）';
COMMENT ON COLUMN "public"."agcim3dproject"."servertype" IS '服务类型（i3s:3dtiles）';
-- ----------------------------
-- Primary Key structure for table ag_house
-- ----------------------------
ALTER TABLE "public"."ag_house" ADD CONSTRAINT "ag_resource_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ag_house_materials
-- ----------------------------
ALTER TABLE "public"."ag_house_materials" ADD CONSTRAINT "ag_house_materials_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ag_house_materials_point
-- ----------------------------
ALTER TABLE "public"."ag_house_materials_point" ADD CONSTRAINT "ag_house_materials_point_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ag_materials_component
-- ----------------------------
ALTER TABLE "public"."ag_materials_component" ADD CONSTRAINT "ag_materials_component_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ag_permission
-- ----------------------------
ALTER TABLE "public"."ag_permission" ADD CONSTRAINT "ag_materials_permission_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ag_sys_setting
-- ----------------------------
ALTER TABLE "public"."ag_sys_setting" ADD CONSTRAINT "ag_sys_setting_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ag_user_design_materials
-- ----------------------------
ALTER TABLE "public"."ag_user_design_materials" ADD CONSTRAINT "ag_user_design_scheme_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ag_user_design_scheme
-- ----------------------------
ALTER TABLE "public"."ag_user_design_scheme" ADD CONSTRAINT "ag_user_design_scheme2_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table ag_user_message
-- ----------------------------
ALTER TABLE "public"."ag_user_message" ADD CONSTRAINT "ag_user_message_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table agcim3dentity_vb3
-- ----------------------------
ALTER TABLE "public"."agcim3dentity_vb3" ADD CONSTRAINT "agcim3dentity_vb2_copy1_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table agcim3dentity_vb4
-- ----------------------------
ALTER TABLE "public"."agcim3dentity_vb4" ADD CONSTRAINT "agcim3dentity_vb3_copy1_pkey" PRIMARY KEY ("id");


-------------2020-11-25 创建agcim3dbuilding表 start (更新20201210)--------------------------
CREATE TABLE "public"."agcim3dbuilding" (
  "id" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "projectid" varchar(200) COLLATE "pg_catalog"."default",
  "buildingname" varchar(200) COLLATE "pg_catalog"."default",
  "location" varchar(255) COLLATE "pg_catalog"."default",
  "createtime" varchar(100) COLLATE "pg_catalog"."default",
  "buildingtype" varchar(100) COLLATE "pg_catalog"."default",
  "usage" varchar(200) COLLATE "pg_catalog"."default",
  "builtuparea" varchar(200) COLLATE "pg_catalog"."default",
  "height" varchar(200) COLLATE "pg_catalog"."default",
  "boundingbox" text COLLATE "pg_catalog"."default",
  "baseline" text COLLATE "pg_catalog"."default",
  "entitytable" varchar(200) COLLATE "pg_catalog"."default",
  "entitycount" varchar(200) COLLATE "pg_catalog"."default",
  "materialtable" varchar(200) COLLATE "pg_catalog"."default",
  "geometrytable" varchar(200) COLLATE "pg_catalog"."default",
  "geometrytype" varchar(200) COLLATE "pg_catalog"."default",
  "buildingecoindex" varchar(200) COLLATE "pg_catalog"."default",
  "metadata" text COLLATE "pg_catalog"."default",
  CONSTRAINT "agcim3dbuilding_pkey1" PRIMARY KEY ("id")
)
;
ALTER TABLE "public"."agcim3dbuilding"
  OWNER TO "postgres";

COMMENT ON COLUMN "public"."agcim3dbuilding"."id" IS 'ID';
COMMENT ON COLUMN "public"."agcim3dbuilding"."projectid" IS '项目id';
COMMENT ON COLUMN "public"."agcim3dbuilding"."buildingname" IS 'BIM建筑名称';
COMMENT ON COLUMN "public"."agcim3dbuilding"."location" IS '位置';
COMMENT ON COLUMN "public"."agcim3dbuilding"."createtime" IS '创建时间';
COMMENT ON COLUMN "public"."agcim3dbuilding"."buildingtype" IS '建筑类型';
COMMENT ON COLUMN "public"."agcim3dbuilding"."usage" IS '建筑用途';
COMMENT ON COLUMN "public"."agcim3dbuilding"."builtuparea" IS '建筑面积';
COMMENT ON COLUMN "public"."agcim3dbuilding"."height" IS '建筑高度';
COMMENT ON COLUMN "public"."agcim3dbuilding"."boundingbox" IS '模型的包围盒';
COMMENT ON COLUMN "public"."agcim3dbuilding"."baseline" IS '模型的基线';
COMMENT ON COLUMN "public"."agcim3dbuilding"."entitytable" IS '实体表的表名(存储建筑所有构件信息)';
COMMENT ON COLUMN "public"."agcim3dbuilding"."entitycount" IS '构件的数量';
COMMENT ON COLUMN "public"."agcim3dbuilding"."materialtable" IS '材质表的表名';
COMMENT ON COLUMN "public"."agcim3dbuilding"."geometrytable" IS '几何表的表名';
COMMENT ON COLUMN "public"."agcim3dbuilding"."geometrytype" IS '几何表的类型';
COMMENT ON COLUMN "public"."agcim3dbuilding"."buildingecoindex" IS '以JSON的形式表示其他经济技术指标，比如容积率，绿地率，楼层数';
COMMENT ON COLUMN "public"."agcim3dbuilding"."metadata" IS '其他信息。JSON字符串';
-------------2020-11-25 创建agcim3dbuilding表 end--------------------------