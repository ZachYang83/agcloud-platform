更新服务需要更新的sql


-------------2020-07-10 添加样式管理start----------------
alter table ag_layer add style_manager_id varchar(72);
CREATE TABLE "ag_style_manager" (
  "id" varchar(80) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "information" text COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default",
  "view_img" text COLLATE "pg_catalog"."default",
  CONSTRAINT "ag_style_manager_pkey" PRIMARY KEY ("id")
);

-------------2020-07-10 添加样式管理end----------------

-------------2020-07-10 添加微件管理start----------------
alter table "public"."opu_rs_func" add func_invoke_http_type varchar(20);
alter table "public"."opu_rs_func" add func_invoke_key varchar(255);
COMMENT ON COLUMN "public"."opu_rs_func"."func_invoke_http_type" IS '接口访问的http_type, 例如GET、POST、DELETE、PUT等,为空默认为全部';
COMMENT ON COLUMN "public"."opu_rs_func"."func_invoke_key" IS '接口自动同步的key,由应用号+全路径+httptype构成，请求的唯一标识';

CREATE TABLE "public"."opu_func_widget_config" (
  "id" varchar(80) COLLATE "pg_catalog"."default" NOT NULL,
  "func_code" varchar(100) COLLATE "pg_catalog"."default",
  "config_key" varchar(50) COLLATE "pg_catalog"."default",
  "config_type" varchar(255) COLLATE "pg_catalog"."default",
  "is_active" varchar(2) COLLATE "pg_catalog"."default",
  "config_value" text COLLATE "pg_catalog"."default",
  CONSTRAINT "opu_func_widget_config_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."opu_func_widget_config"
  OWNER TO "postgres";
COMMENT ON COLUMN "public"."opu_func_widget_config"."id" IS '主键';
COMMENT ON COLUMN "public"."opu_func_widget_config"."func_code" IS '微件编码';
COMMENT ON COLUMN "public"."opu_func_widget_config"."config_key" IS '参数标识';
COMMENT ON COLUMN "public"."opu_func_widget_config"."config_type" IS '参数类型';
COMMENT ON COLUMN "public"."opu_func_widget_config"."is_active" IS '是否激活使用 "1":激活  “0”:未激活';
COMMENT ON COLUMN "public"."opu_func_widget_config"."config_value" IS '参数值';
-------------2020-07-10 添加微件管理end----------------


-------------2020-07-10 修改opu_om_user表 start----------------
alter table "public"."opu_om_user" add root_org_id varchar(100);
-------------2020-07-10 修改opu_om_user表end----------------


-------------2020-07-13 修改opu_func_widget_config表 start----------------
alter table "public"."opu_func_widget_config" add func_invoke_url varchar(100);
COMMENT ON COLUMN "public"."opu_func_widget_config"."func_invoke_url" IS '微件访问地址';
-------------2020-07-13 修改opu_func_widget_config表end----------------

-------------2020-07-14 添加ag_project_manager表 start----------------
CREATE TABLE "public"."ag_project_manager" (
  "id" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(50) COLLATE "pg_catalog"."default",
  "extend_data" text COLLATE "pg_catalog"."default",
  "creator" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default",
  "is_delete" varchar(5) COLLATE "pg_catalog"."default",
  CONSTRAINT "ag_project_manager_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."ag_project_manager"
  OWNER TO "postgres";
COMMENT ON COLUMN "public"."ag_project_manager"."id" IS 'id主键';
COMMENT ON COLUMN "public"."ag_project_manager"."name" IS '工程名称';
COMMENT ON COLUMN "public"."ag_project_manager"."type" IS '工程类型(baseInfo：基础信息；UIStyle：界面风格；netConfig：网络管理)';
COMMENT ON COLUMN "public"."ag_project_manager"."extend_data" IS '扩展字段（json字符串格式，名称：值：说明）';
COMMENT ON COLUMN "public"."ag_project_manager"."creator" IS '创建者';
COMMENT ON COLUMN "public"."ag_project_manager"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_project_manager"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_project_manager"."remark" IS '备注';
COMMENT ON COLUMN "public"."ag_project_manager"."is_delete" IS '是否删除（“0”：未删除； “1”：已删除）';
-------------2020-07-14 添加ag_project_manager表 end----------------

-------------2020-08-12 添加字段ag_layer表 start----------------
alter table ag_layer add layer_version varchar(255);
COMMENT ON COLUMN "public"."ag_layer"."layer_version" IS '图层版本号';
alter table ag_layer add layer_aggregate_name varchar(255);
COMMENT ON COLUMN "public"."ag_layer"."layer_aggregate_name" IS '聚合名称';
-------------2020-08-12 添加字段ag_layer表 start----------------

-------------2020-08-28 添加表 start----------------
CREATE TABLE "public"."ag_widget_store" (
  "id" varchar(72) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "route_name" varchar(255) COLLATE "pg_catalog"."default",
  "description" text COLLATE "pg_catalog"."default",
  "version" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "thumb" bytea,
  "widget_name" varchar(255) COLLATE "pg_catalog"."default",
  "size" varchar(255) COLLATE "pg_catalog"."default",
  "widget_file" bytea,
  "thumb_name" varchar(255) COLLATE "pg_catalog"."default",
  CONSTRAINT "ag_widget_store_pkey" PRIMARY KEY ("id")
)
;
ALTER TABLE "public"."ag_widget_store"
  OWNER TO "postgres";
COMMENT ON COLUMN "public"."ag_widget_store"."name" IS '微件商店名称';
COMMENT ON COLUMN "public"."ag_widget_store"."route_name" IS '路由名称（以压缩包名称命名）';
COMMENT ON COLUMN "public"."ag_widget_store"."description" IS '功能描述';
COMMENT ON COLUMN "public"."ag_widget_store"."version" IS '版本';
COMMENT ON COLUMN "public"."ag_widget_store"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_widget_store"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_widget_store"."thumb" IS '缩略图';
COMMENT ON COLUMN "public"."ag_widget_store"."widget_name" IS '微件文件源文件名称';
COMMENT ON COLUMN "public"."ag_widget_store"."size" IS '文件大小';
COMMENT ON COLUMN "public"."ag_widget_store"."widget_file" IS '微件文件';
COMMENT ON COLUMN "public"."ag_widget_store"."thumb_name" IS '缩略图源文件名称';
-------------2020-08-28 添加字段表 end----------------


-------------2020-08-28 添加ag_problem_discern表 start----------------
CREATE TABLE "public"."ag_problem_discern" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "problem_img" bytea,
  "description" text COLLATE "pg_catalog"."default",
  "img_id" varchar(64) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  CONSTRAINT "ag_problem_discern_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."ag_problem_discern"
  OWNER TO "postgres";
COMMENT ON COLUMN "public"."ag_problem_discern"."id" IS '主键';
COMMENT ON COLUMN "public"."ag_problem_discern"."problem_img" IS '问题图片二进制';
COMMENT ON COLUMN "public"."ag_problem_discern"."description" IS '描述';
COMMENT ON COLUMN "public"."ag_problem_discern"."img_id" IS '图片唯一标识';
COMMENT ON COLUMN "public"."ag_problem_discern"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_problem_discern"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_problem_discern"."remark" IS '备注';
-------------2020-08-28 添加ag_problem_discern表 end----------------


-------------2020-09-07 添加ag_building_component表 start----------------
CREATE TABLE "public"."ag_building_component" (
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "table_code" varchar(10) COLLATE "pg_catalog"."default",
  "large_code" varchar(10) COLLATE "pg_catalog"."default",
  "medium_code" varchar(10) COLLATE "pg_catalog"."default",
  "small_code" varchar(10) COLLATE "pg_catalog"."default",
  "detail_code" varchar(10) COLLATE "pg_catalog"."default",
  "chinese_name" varchar(100) COLLATE "pg_catalog"."default",
  "english_name" varchar(100) COLLATE "pg_catalog"."default",
  "type" varchar(50) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  CONSTRAINT "ag_building_component_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."ag_building_component"
  OWNER TO "postgres";
COMMENT ON COLUMN "public"."ag_building_component"."id" IS '主键id';
COMMENT ON COLUMN "public"."ag_building_component"."table_code" IS '表代码(两位阿拉伯数字：01)';
COMMENT ON COLUMN "public"."ag_building_component"."large_code" IS '大类代码(两位阿拉伯数字：01)';
COMMENT ON COLUMN "public"."ag_building_component"."medium_code" IS '中类代码(两位阿拉伯数字：01)';
COMMENT ON COLUMN "public"."ag_building_component"."small_code" IS '小类代码(两位阿拉伯数字：01)';
COMMENT ON COLUMN "public"."ag_building_component"."detail_code" IS '细类代码(两位阿拉伯数字：01)';
COMMENT ON COLUMN "public"."ag_building_component"."chinese_name" IS '类目中文';
COMMENT ON COLUMN "public"."ag_building_component"."english_name" IS '类目英文';
COMMENT ON COLUMN "public"."ag_building_component"."type" IS '建筑构件分类编码（对应数字字典）';
COMMENT ON COLUMN "public"."ag_building_component"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_building_component"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_building_component"."remark" IS '备注';
COMMENT ON TABLE "public"."ag_building_component" IS '构件分类表';
-------------2020-09-07 添加ag_building_component表 end----------------
-------------2020-09-23 修改ag_widget_store表 start----------------
alter table ag_widget_store add en_name varchar(255);
COMMENT ON COLUMN "public"."ag_widget_store"."en_name" IS '英文名称';
alter table ag_widget_store add expand varchar(255);
COMMENT ON COLUMN "public"."ag_widget_store"."expand" IS '拓展';
-------------2020-09-23 修改ag_widget_store表 end----------------

-------------2020-10-15 添加ag_server_content表 start----------------
CREATE TABLE "public"."ag_server_content" (
  "id" varchar(72) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" varchar(72) COLLATE "pg_catalog"."default",
  "title" varchar(255) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(72) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "size" varchar(72) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "tags" varchar(255) COLLATE "pg_catalog"."default",
  "group_id" varchar(72) COLLATE "pg_catalog"."default",
  CONSTRAINT "content_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."ag_server_content"
  OWNER TO "postgres";
COMMENT ON COLUMN "public"."ag_server_content"."id" IS '主键id';
COMMENT ON COLUMN "public"."ag_server_content"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."ag_server_content"."title" IS '标题';
COMMENT ON COLUMN "public"."ag_server_content"."name" IS '名称（源文件名）';
COMMENT ON COLUMN "public"."ag_server_content"."type" IS '服务类型';
COMMENT ON COLUMN "public"."ag_server_content"."path" IS '服务文件对应的本地路径';
COMMENT ON COLUMN "public"."ag_server_content"."size" IS '文件大小';
COMMENT ON COLUMN "public"."ag_server_content"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_server_content"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_server_content"."remark" IS '备注';
COMMENT ON COLUMN "public"."ag_server_content"."tags" IS '标签（可以多个，逗号隔开，如（标签1，标签2））';
COMMENT ON COLUMN "public"."ag_server_content"."group_id" IS '服务分组id';
COMMENT ON TABLE "public"."ag_server_content" IS '服务内容管理表';
-------------2020-10-15 添加ag_server_content表 end----------------

-------------2020-10-15 添加ag_server_content_group表 start----------------
CREATE TABLE "public"."ag_server_content_group" (
  "id" varchar(72) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  CONSTRAINT "ag_server_content_group_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."ag_server_content_group"
  OWNER TO "postgres";
COMMENT ON COLUMN "public"."ag_server_content_group"."id" IS '分组id';
COMMENT ON COLUMN "public"."ag_server_content_group"."name" IS '分组名称';
COMMENT ON COLUMN "public"."ag_server_content_group"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_server_content_group"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_server_content_group"."remark" IS '备注';
COMMENT ON TABLE "public"."ag_server_content_group" IS '服务内容分组表';

-- 初始化内容分组数据
INSERT INTO "public"."ag_server_content_group"(id,name,remark,create_time,modify_time) VALUES
 ('0','所有我的内容','所有',now(),now()),('1','augurit','默认',now(),now()),('2','BIM审查系统','BIM审查系统',now(),now());
-------------2020-10-15 添加ag_server_content_group表 end----------------

-------------2020-10-15 添加ag_server_effect表 start----------------
CREATE TABLE "public"."ag_server_effect" (
  "id" varchar(72) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "user_id" varchar(72) COLLATE "pg_catalog"."default",
  "type" varchar(72) COLLATE "pg_catalog"."default",
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "size" varchar(72) COLLATE "pg_catalog"."default",
  "tags" varchar(255) COLLATE "pg_catalog"."default",
  "group_id" varchar(72) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  CONSTRAINT "ag_server_effect_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."ag_server_effect"
  OWNER TO "postgres";
COMMENT ON COLUMN "public"."ag_server_effect"."id" IS '主键';
COMMENT ON COLUMN "public"."ag_server_effect"."name" IS '名称（源文件名）';
COMMENT ON COLUMN "public"."ag_server_effect"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."ag_server_effect"."type" IS '文件类型';
COMMENT ON COLUMN "public"."ag_server_effect"."url" IS '文件访问路径';
COMMENT ON COLUMN "public"."ag_server_effect"."size" IS '文件大小';
COMMENT ON COLUMN "public"."ag_server_effect"."tags" IS '标签';
COMMENT ON COLUMN "public"."ag_server_effect"."group_id" IS '分组id';
COMMENT ON COLUMN "public"."ag_server_effect"."remark" IS '备注';
COMMENT ON COLUMN "public"."ag_server_effect"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_server_effect"."modify_time" IS '修改时间';
COMMENT ON TABLE "public"."ag_server_effect" IS '服务管理-效果表';
-------------2020-10-15 添加ag_server_effect表 end----------------

-------------2020-10-15 添加ag_server_effect_group表 start----------------
CREATE TABLE "public"."ag_server_effect_group" (
  "id" varchar(72) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  CONSTRAINT "ag_server_effect_group_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."ag_server_effect_group"
  OWNER TO "postgres";
COMMENT ON COLUMN "public"."ag_server_effect_group"."id" IS '主键';
COMMENT ON COLUMN "public"."ag_server_effect_group"."name" IS '分组名称';
COMMENT ON COLUMN "public"."ag_server_effect_group"."remark" IS '备注';
COMMENT ON COLUMN "public"."ag_server_effect_group"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_server_effect_group"."modify_time" IS '修改时间';
COMMENT ON TABLE "public"."ag_server_effect_group" IS '服务管理-效果分组表';

-- 初始化效果分组数据
INSERT INTO "public"."ag_server_effect_group"(id,name,remark,create_time,modify_time) VALUES
 ('0','所有我的效果','所有',now(),now()),('1','augurit','默认',now(),now());
-------------2020-10-15 添加ag_server_effect_group表 end----------------

-------------2020-10-21 修改ag_widget_store表 start----------------
alter table ag_widget_store add status int4;
COMMENT ON COLUMN "public"."ag_widget_store"."status" IS '1未审核；2审核通过；3审核拒绝';

-------------2020-10-21 修改ag_widget_store表 end----------------

-------------2020-10-24 修改ag_widget_store表 start----------------
alter table ag_widget_store add md_file bytea;
COMMENT ON COLUMN "public"."ag_widget_store"."md_file" IS 'md二进制文件';
-------------2020-10-24 修改ag_widget_store表 end----------------

-------------2020-10-31 bim审查数据表创建 start----------------
CREATE TABLE "public"."ag_bim_check" (
  "id" varchar(80)  NOT NULL,
  "source_id" varchar(255) ,
  "type" varchar(255) ,
  "status" varchar(255) ,
  "basis" text ,
  "result" varchar(255) ,
  "name" varchar(255) ,
  "method" varchar(255) ,
  "article_id" varchar(255) ,
  "classification_type" varchar(255) ,
  "elements" text ,
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255),
  CONSTRAINT "ag_bim_check_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."ag_bim_check"
  OWNER TO "postgres";
COMMENT ON COLUMN "public"."ag_bim_check"."id" IS '主键';
COMMENT ON COLUMN "public"."ag_bim_check"."source_id" IS '来源id';
COMMENT ON COLUMN "public"."ag_bim_check"."type" IS '规范分类';
COMMENT ON COLUMN "public"."ag_bim_check"."status" IS '数据状态';
COMMENT ON COLUMN "public"."ag_bim_check"."basis" IS '条文描述';
COMMENT ON COLUMN "public"."ag_bim_check"."result" IS '审查结果描述';
COMMENT ON COLUMN "public"."ag_bim_check"."name" IS '条文编号';
COMMENT ON COLUMN "public"."ag_bim_check"."method" IS '条文所属方法';
COMMENT ON COLUMN "public"."ag_bim_check"."article_id" IS '条文id';
COMMENT ON COLUMN "public"."ag_bim_check"."classification_type" IS '规范类型';
COMMENT ON COLUMN "public"."ag_bim_check"."elements" IS '问题构件列表';
COMMENT ON COLUMN "public"."ag_bim_check"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_bim_check"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_bim_check"."remark" IS '备注';

-------------2020-10-31 bim审查数据表创建 end----------------

-------------2020-11-2 bim审查项目数据表创建 start----------------
CREATE TABLE "public"."ag_bim_check_project" (
  "id" varchar(80)  NOT NULL,
  "name" varchar(255) ,
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255),
  CONSTRAINT "ag_bim_check_project_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."ag_bim_check_project"
  OWNER TO "postgres";
COMMENT ON COLUMN "public"."ag_bim_check_project"."id" IS '主键';
COMMENT ON COLUMN "public"."ag_bim_check_project"."name" IS '审查项目名';
COMMENT ON COLUMN "public"."ag_bim_check_project"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_bim_check_project"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_bim_check_project"."remark" IS '备注';

-------------2020-11-2 bim审查项目数据表创建 end----------------

-------------2020-11-3 bim审查项目模型数据表创建 start----------------
CREATE TABLE "public"."ag_bim_check_project_model" (
  "id" varchar(72) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "ag_bim_check_project_id" varchar(72) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  CONSTRAINT "ag_bim_check_project_model_pkey" PRIMARY KEY ("id")
)
;
ALTER TABLE "public"."ag_bim_check_project_model"
  OWNER TO "postgres";
COMMENT ON COLUMN "public"."ag_bim_check_project_model"."id" IS '主键';
COMMENT ON COLUMN "public"."ag_bim_check_project_model"."name" IS '名称';
COMMENT ON COLUMN "public"."ag_bim_check_project_model"."path" IS '3dtiles模型文件存储路径';
COMMENT ON COLUMN "public"."ag_bim_check_project_model"."ag_bim_check_project_id" IS 'bim审查项目对应id';
COMMENT ON COLUMN "public"."ag_bim_check_project_model"."remark" IS '备注';
COMMENT ON COLUMN "public"."ag_bim_check_project_model"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_bim_check_project_model"."modify_time" IS '修改时间';
COMMENT ON TABLE "public"."ag_bim_check_project_model" IS 'BIM审查项目对应的模型';
-------------2020-11-3 bim审查项目模型数据表创建 end----------------

-------------2020-11-10 修改ag_problem_discern表 start----------------
alter table ag_problem_discern add p_type varchar(10);
COMMENT ON COLUMN "public"."ag_problem_discern"."p_type" IS '1:BIM审查；2：标签管理';
-------------2020-11-10 修改ag_problem_discern表 end----------------


-------------2020-11-10 修改"public"."ag_server_content"表 start----------------
alter table ag_server_content add source_type varchar(72);
alter table ag_server_content add source_rel_id varchar(72);

COMMENT ON COLUMN "public"."ag_server_content"."source_type" IS '文件来源。1：agcim服务管理； 2：BIM审查';
COMMENT ON COLUMN "public"."ag_server_content"."source_rel_id" IS '文件来源关联的业务ID （BIM审查关联BIM审查项目id）';
-------------2020-11-10 修改"public"."ag_server_content"表 end----------------


-------------2020-11-13 修改ag_style_manager表 start----------------
alter table "public"."ag_style_manager" add layer_type varchar(72);

COMMENT ON COLUMN "public"."ag_style_manager"."id" IS 'id';
COMMENT ON COLUMN "public"."ag_style_manager"."name" IS '名称';
COMMENT ON COLUMN "public"."ag_style_manager"."information" IS '样式的json信息';
COMMENT ON COLUMN "public"."ag_style_manager"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_style_manager"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_style_manager"."remark" IS '备注';
COMMENT ON COLUMN "public"."ag_style_manager"."view_img" IS '样式示例图';
COMMENT ON COLUMN "public"."ag_style_manager"."layer_type" IS '可应用该样式的图层类型';
-------------2020-11-13 修改ag_style_manager表 end----------------

-------------2020-11-20 修改ag_bim_check_project表 start----------------
alter table ag_bim_check_project add json_data text;
COMMENT ON COLUMN "public"."ag_bim_check_project"."json_data" IS '存放json文件';
-------------2020-11-20 修改ag_bim_check_project表 end----------------


-------------2020-11-24 修改ag_bim_check_project_model表 start----------------
alter table ag_bim_check_project_model add info_rel_table_name varchar(255);
COMMENT ON COLUMN "public"."ag_bim_check_project_model"."info_rel_table_name" IS '模型信息关联的表名称';
-------------2020-11-24 修改ag_bim_check_project_model表 end----------------

------------2020-12-4 建表 ag_widget_assets_ ----START-----
CREATE TABLE "public"."ag_widget_assets_project" (
  "id" varchar(80)  NOT NULL,
  "app_soft_id" varchar(80) ,
  "unique_idf" varchar(80),
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255),
  CONSTRAINT "ag_widget_assets_project_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."ag_widget_assets_project"
  OWNER TO "postgres";
COMMENT ON COLUMN "public"."ag_widget_assets_project"."id" IS '主键';
COMMENT ON COLUMN "public"."ag_widget_assets_project"."app_soft_id" IS '项目编号';
COMMENT ON COLUMN "public"."ag_widget_assets_project"."unique_idf" IS '项目唯一标识';
COMMENT ON COLUMN "public"."ag_widget_assets_project"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_widget_assets_project"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_widget_assets_project"."remark" IS '备注';


CREATE TABLE "public"."ag_widget_assets_columns" (
  "id" varchar(80)  NOT NULL,
  "thematic_table_id" varchar(255) ,
  "column_name" varchar(255) ,
  "column_type" varchar(255) ,
  "create_time" timestamp(6) ,
  CONSTRAINT "ag_widget_assets_columns_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "public"."ag_widget_assets_columns"."id" IS '主键';
COMMENT ON COLUMN "public"."ag_widget_assets_columns"."thematic_table_id" IS '专题表id';
COMMENT ON COLUMN "public"."ag_widget_assets_columns"."column_name" IS '字段名称';
COMMENT ON COLUMN "public"."ag_widget_assets_columns"."column_type" IS '字段类型';
COMMENT ON COLUMN "public"."ag_widget_assets_columns"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."ag_widget_assets_columns" IS '专题字段表';

CREATE TABLE "public"."ag_widget_assets_table" (
  "id" varchar(80)  NOT NULL,
  "app_soft_id" varchar(255) ,
  "table_name" varchar(255) ,
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255),
  CONSTRAINT "ag_widget_assets_table_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "public"."ag_widget_assets_table"."id" IS '主键';
COMMENT ON COLUMN "public"."ag_widget_assets_table"."app_soft_id" IS '项目应用id';
COMMENT ON COLUMN "public"."ag_widget_assets_table"."table_name" IS '表名（当前项目下的表名唯一）';
COMMENT ON COLUMN "public"."ag_widget_assets_table"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_widget_assets_table"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_widget_assets_table"."remark" IS '备注';
COMMENT ON TABLE "public"."ag_widget_assets_table" IS '专题表';

------------2020-12-4 建表 ag_widget_assets_ ----END-----

------------2020-12-18 建表 ag_bim_check_standard ----START-----
CREATE TABLE "public"."ag_bim_check_standard" (
  "id" varchar(80)  COLLATE "pg_catalog"."default" NOT NULL,
  "clause" varchar(255) COLLATE "pg_catalog"."default",
  "serial" varchar(80) COLLATE "pg_catalog"."default",
  "enforce" varchar(10) COLLATE "pg_catalog"."default",
  "clause_content" text COLLATE "pg_catalog"."default",
  "associate_model" varchar(255) COLLATE "pg_catalog"."default",
  "clause_category" varchar(80) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  CONSTRAINT "ag_bim_check_standard_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "public"."ag_bim_check_standard"."id" IS '主键';
COMMENT ON COLUMN "public"."ag_bim_check_standard"."clause" IS '规范审查条文';
COMMENT ON COLUMN "public"."ag_bim_check_standard"."serial" IS '条文序号';
COMMENT ON COLUMN "public"."ag_bim_check_standard"."enforce" IS '是否强条';
COMMENT ON COLUMN "public"."ag_bim_check_standard"."clause_content" IS '条文内容拆解';
COMMENT ON COLUMN "public"."ag_bim_check_standard"."associate_model" IS '关联模型信息';
COMMENT ON COLUMN "public"."ag_bim_check_standard"."clause_category" IS '条文内容所属类型';
COMMENT ON COLUMN "public"."ag_bim_check_standard"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_bim_check_standard"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_bim_check_standard"."remark" IS '备注';
COMMENT ON TABLE "public"."ag_bim_check_standard" IS '审查规范表';
------------2020-12-18 建表 ag_bim_check_standard ----END-----


------------2020-12-28 建表  ----START-----
CREATE TABLE "public"."ag_identify_mancar_source" (
  "id" varchar(80)  COLLATE "pg_catalog"."default" NOT NULL,
  "status" varchar(255) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "file_name" varchar(255) COLLATE "pg_catalog"."default",
  "file_path" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  CONSTRAINT "ag_identify_mancar_source_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "public"."ag_identify_mancar_source"."id" IS '主键';
COMMENT ON COLUMN "public"."ag_identify_mancar_source"."status" IS '0未识别；1已识别';
COMMENT ON COLUMN "public"."ag_identify_mancar_source"."name" IS '资源名称';
COMMENT ON COLUMN "public"."ag_identify_mancar_source"."file_name" IS '文件名称';
COMMENT ON COLUMN "public"."ag_identify_mancar_source"."file_path" IS '文件存储路径';
COMMENT ON COLUMN "public"."ag_identify_mancar_source"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_identify_mancar_source"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_identify_mancar_source"."remark" IS '备注';
COMMENT ON TABLE "public"."ag_identify_mancar_source" IS '人车识别资源表';

CREATE TABLE "public"."ag_identify_mancar_result" (
  "id" varchar(80)  COLLATE "pg_catalog"."default" NOT NULL,
  "source_id" varchar(255) COLLATE "pg_catalog"."default",
  "identify_time" timestamp(6),
  "num_people" int4 ,
  "num_car" int4 ,
  "create_time" timestamp(6),
  "modify_time" timestamp(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  CONSTRAINT "ag_identify_mancar_result_pkey" PRIMARY KEY ("id")
)
;
COMMENT ON COLUMN "public"."ag_identify_mancar_result"."id" IS '主键';
COMMENT ON COLUMN "public"."ag_identify_mancar_result"."source_id" IS '人车识别资源表主键id';
COMMENT ON COLUMN "public"."ag_identify_mancar_result"."identify_time" IS '识别时间';
COMMENT ON COLUMN "public"."ag_identify_mancar_result"."num_people" IS '识别人数';
COMMENT ON COLUMN "public"."ag_identify_mancar_result"."num_car" IS '识别车数';
COMMENT ON COLUMN "public"."ag_identify_mancar_result"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ag_identify_mancar_result"."modify_time" IS '修改时间';
COMMENT ON COLUMN "public"."ag_identify_mancar_result"."remark" IS '备注';
COMMENT ON TABLE "public"."ag_identify_mancar_result" IS '人车识别结果表';
------------2020-12-28 建表 ----END-----