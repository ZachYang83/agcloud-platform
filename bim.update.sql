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

COMMENT ON COLUMN "public"."ag_house"."id" IS '??????ID';
COMMENT ON COLUMN "public"."ag_house"."source_name" IS '???????????????';
COMMENT ON COLUMN "public"."ag_house"."store_full_path" IS '??????????????????';
COMMENT ON COLUMN "public"."ag_house"."suffix" IS '????????????';
COMMENT ON COLUMN "public"."ag_house"."size" IS '???????????????';
COMMENT ON COLUMN "public"."ag_house"."type" IS '1??????????????????2??????????????????3?????????';
COMMENT ON COLUMN "public"."ag_house"."category_id" IS '??????id';
COMMENT ON COLUMN "public"."ag_house"."create_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_house"."modify_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_house"."remark" IS '??????';
COMMENT ON COLUMN "public"."ag_house"."old_name" IS '???????????????';
COMMENT ON COLUMN "public"."ag_house"."hourse_name" IS '????????????';
COMMENT ON COLUMN "public"."ag_house"."homestead_area" IS '??????????????????????????????';
COMMENT ON COLUMN "public"."ag_house"."floor_area" IS '???????????????????????????';
COMMENT ON COLUMN "public"."ag_house"."covered_area" IS '???????????????????????????';
COMMENT ON COLUMN "public"."ag_house"."cost_estimates" IS '???????????????????????????';
COMMENT ON COLUMN "public"."ag_house"."is_show" IS '????????????????????????1?????????0????????????';
COMMENT ON COLUMN "public"."ag_house"."source_id" IS '??????????????????id???????????????';
COMMENT ON COLUMN "public"."ag_house"."user_id" IS '??????id';
COMMENT ON COLUMN "public"."ag_house"."thumb" IS '?????????base64';
COMMENT ON COLUMN "public"."ag_house"."table_name" IS '????????????????????????';
COMMENT ON COLUMN "public"."ag_house"."structure_type" IS '????????????';
COMMENT ON COLUMN "public"."ag_house"."model_size" IS '????????????';
COMMENT ON COLUMN "public"."ag_house"."status" IS '1???rft???2???3dtiles';
COMMENT ON COLUMN "public"."ag_house"."component_code" IS '??????????????????????????????-??????.??????.??????.??????';
COMMENT ON COLUMN "public"."ag_house"."component_code_name" IS '????????????????????????????????????-??????.??????.??????.??????';

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
COMMENT ON COLUMN "public"."ag_house_materials"."id" IS '??????id';
COMMENT ON COLUMN "public"."ag_house_materials"."source_id" IS '??????id';
COMMENT ON COLUMN "public"."ag_house_materials"."name" IS '??????';
COMMENT ON COLUMN "public"."ag_house_materials"."url" IS '??????url';
COMMENT ON COLUMN "public"."ag_house_materials"."type" IS '?????????1????????????2?????????3????????????4????????????';
COMMENT ON COLUMN "public"."ag_house_materials"."create_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_house_materials"."modify_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_house_materials"."remark" IS '??????';

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
COMMENT ON COLUMN "public"."ag_house_materials_point"."id" IS '??????id';
COMMENT ON COLUMN "public"."ag_house_materials_point"."source_id" IS '??????id';
COMMENT ON COLUMN "public"."ag_house_materials_point"."name" IS '??????';
COMMENT ON COLUMN "public"."ag_house_materials_point"."coordinates" IS '????????????';
COMMENT ON COLUMN "public"."ag_house_materials_point"."url" IS '??????url';
COMMENT ON COLUMN "public"."ag_house_materials_point"."feature" IS '??????';
COMMENT ON COLUMN "public"."ag_house_materials_point"."floor" IS '??????';
COMMENT ON COLUMN "public"."ag_house_materials_point"."create_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_house_materials_point"."modify_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_house_materials_point"."remark" IS '??????';

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
COMMENT ON COLUMN "public"."ag_materials_component"."id" IS '??????ID';
COMMENT ON COLUMN "public"."ag_materials_component"."objectid" IS 'objectid';
COMMENT ON COLUMN "public"."ag_materials_component"."name" IS '????????????';
COMMENT ON COLUMN "public"."ag_materials_component"."version" IS '??????';
COMMENT ON COLUMN "public"."ag_materials_component"."infotype" IS '????????? agRoom???agWall';
COMMENT ON COLUMN "public"."ag_materials_component"."profession" IS '?????????????????????????????????';
COMMENT ON COLUMN "public"."ag_materials_component"."level" IS '??????';
COMMENT ON COLUMN "public"."ag_materials_component"."catagory" IS '???????????????????????????????????????????????????????????????????????????';
COMMENT ON COLUMN "public"."ag_materials_component"."familyname" IS '?????????';
COMMENT ON COLUMN "public"."ag_materials_component"."familytype" IS '?????????';
COMMENT ON COLUMN "public"."ag_materials_component"."materialid" IS '??????id';
COMMENT ON COLUMN "public"."ag_materials_component"."elementattributes" IS '?????????????????????';
COMMENT ON COLUMN "public"."ag_materials_component"."categorypath" IS '????????????';
COMMENT ON COLUMN "public"."ag_materials_component"."geometry" IS '???????????????????????????????????????gltf?????????????????????????????????????????????glb';
COMMENT ON COLUMN "public"."ag_materials_component"."topologyelements" IS '?????????????????????????????????????????????x???y???z???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????';
COMMENT ON COLUMN "public"."ag_materials_component"."boundingbox" IS '????????????????????????????????????????????????????????????????????????';
COMMENT ON COLUMN "public"."ag_materials_component"."glb" IS 'glb??????';
COMMENT ON COLUMN "public"."ag_materials_component"."texture" IS '??????';
COMMENT ON COLUMN "public"."ag_materials_component"."measure" IS '????????????x???x??????';
COMMENT ON COLUMN "public"."ag_materials_component"."vendor" IS '??????';
COMMENT ON COLUMN "public"."ag_materials_component"."single_price" IS '???????????????';
COMMENT ON COLUMN "public"."ag_materials_component"."thumb" IS '?????????';
COMMENT ON COLUMN "public"."ag_materials_component"."table_name" IS '????????????????????????';
COMMENT ON COLUMN "public"."ag_materials_component"."create_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_materials_component"."modify_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_materials_component"."remark" IS '??????';
COMMENT ON COLUMN "public"."ag_materials_component"."thumb_file_name" IS '??????????????????';
COMMENT ON COLUMN "public"."ag_materials_component"."glb_file_name" IS 'glb?????????';
COMMENT ON COLUMN "public"."ag_materials_component"."status" IS '1???rfa???2???glb';
COMMENT ON COLUMN "public"."ag_materials_component"."model_size" IS '????????????';
COMMENT ON COLUMN "public"."ag_materials_component"."component_code" IS '??????????????????????????????-??????.??????.??????.??????';
COMMENT ON COLUMN "public"."ag_materials_component"."component_code_name" IS '????????????????????????????????????-??????.??????.??????.??????';
COMMENT ON COLUMN "public"."ag_materials_component"."specification" IS '???x???x???';

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
COMMENT ON COLUMN "public"."ag_permission"."source_id" IS '??????id';
COMMENT ON COLUMN "public"."ag_permission"."code" IS '???????????????code???';
COMMENT ON COLUMN "public"."ag_permission"."type" IS '1?????????2?????????3??????';

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
COMMENT ON COLUMN "public"."ag_user_design_materials"."type" IS '1???????????????2???????????????3????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."name" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."position" IS '??????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."orientation" IS '??????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."url" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."model_matrix" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."property_url" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."user_id" IS '??????id';
COMMENT ON COLUMN "public"."ag_user_design_materials"."create_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."modify_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."remark" IS '??????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."design_scheme_id" IS 'ag_user_design_scheme?????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."angle" IS '??????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."table_name" IS '????????????????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."components" IS '??????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."style" IS '??????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."tile_url" IS 'tile url';
COMMENT ON COLUMN "public"."ag_user_design_materials"."component_type" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."component_id" IS '??????id???????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."boundingbox" IS '?????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."topologyelements" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."obb_center" IS 'obb???????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."subtract" IS 'rvt????????????????????????3dtiles????????????????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."relation_ids" IS '?????????????????????????????????????????????????????????????????????????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."measure" IS '????????????x???x??????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."size" IS '???????????????,??????????????????????????????????????????';
COMMENT ON COLUMN "public"."ag_user_design_materials"."clip_matrix" IS '?????????????????????';

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
COMMENT ON COLUMN "public"."ag_user_design_scheme"."name" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."land_info" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."location" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."description" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."thumb" IS '?????????????????????';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."user_id" IS '??????id';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."create_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."modify_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_design_scheme"."is_default" IS '??????????????????0??????1??????';

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
COMMENT ON COLUMN "public"."ag_user_message"."user_id" IS '??????id';
COMMENT ON COLUMN "public"."ag_user_message"."user_type" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_message"."organization" IS '??????';
COMMENT ON COLUMN "public"."ag_user_message"."security_question" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_message"."security_answer" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_message"."create_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_message"."modify_time" IS '????????????';
COMMENT ON COLUMN "public"."ag_user_message"."remark" IS '??????';

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
-- Table structure for agcim3dproject  (??????20201210)
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

COMMENT ON COLUMN "public"."agcim3dproject"."id" IS '??????ID';
COMMENT ON COLUMN "public"."agcim3dproject"."name" IS '????????????';
COMMENT ON COLUMN "public"."agcim3dproject"."units" IS '?????????????????????';
COMMENT ON COLUMN "public"."agcim3dproject"."address" IS '???????????????????????????';
COMMENT ON COLUMN "public"."agcim3dproject"."owner" IS '?????????????????????JSON???';
COMMENT ON COLUMN "public"."agcim3dproject"."creattime" IS '?????????????????????';
COMMENT ON COLUMN "public"."agcim3dproject"."projectphase" IS '????????????';
COMMENT ON COLUMN "public"."agcim3dproject"."constructiontype" IS '????????????:??????;??????;??????;';
COMMENT ON COLUMN "public"."agcim3dproject"."ecoindex" IS '??????????????????';
COMMENT ON COLUMN "public"."agcim3dproject"."ecoindextable" IS '??????????????????????????????????????????';
COMMENT ON COLUMN "public"."agcim3dproject"."metadata" IS '???????????????????????????????????????????????????JSON?????????';
COMMENT ON COLUMN "public"."agcim3dproject"."serverurl" IS '???????????????i3s:3dtiles???';
COMMENT ON COLUMN "public"."agcim3dproject"."servertype" IS '???????????????i3s:3dtiles???';
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


-------------2020-11-25 ??????agcim3dbuilding??? start (??????20201210)--------------------------
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
COMMENT ON COLUMN "public"."agcim3dbuilding"."projectid" IS '??????id';
COMMENT ON COLUMN "public"."agcim3dbuilding"."buildingname" IS 'BIM????????????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."location" IS '??????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."createtime" IS '????????????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."buildingtype" IS '????????????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."usage" IS '????????????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."builtuparea" IS '????????????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."height" IS '????????????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."boundingbox" IS '??????????????????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."baseline" IS '???????????????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."entitytable" IS '??????????????????(??????????????????????????????)';
COMMENT ON COLUMN "public"."agcim3dbuilding"."entitycount" IS '???????????????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."materialtable" IS '??????????????????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."geometrytable" IS '??????????????????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."geometrytype" IS '??????????????????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."buildingecoindex" IS '???JSON?????????????????????????????????????????????????????????????????????????????????';
COMMENT ON COLUMN "public"."agcim3dbuilding"."metadata" IS '???????????????JSON?????????';
-------------2020-11-25 ??????agcim3dbuilding??? end--------------------------