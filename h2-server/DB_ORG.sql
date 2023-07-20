drop table if exists dictionary;

drop table if exists dictionaryItem;

drop table if exists organization;

drop table if exists permission;

drop table if exists role;

drop table if exists rolePermission;

drop table if exists `user`;

/*==============================================================*/
/* Table: dictionary                                            */
/*==============================================================*/
create table dictionary
(
   id                   int not null auto_increment  comment '',
   code                 varchar(50) not null  comment '',
   name                 varchar(50) not null  comment '',
   `value`              varchar(50)  comment '',
   type                 tinyint not null  comment '枚举值：1——参数（单值）；2——映射（多值）',
   enabled              bool not null  comment '',
   createdDate          timestamp not null  comment '',
   createdBy            varchar(20) not null  comment '',
   lastModifiedDate     timestamp not null  comment '',
   lastModifiedBy       varchar(20) not null  comment '',
   version              int not null  comment '',
   description          varchar(500)  comment '',
   primary key (id)
);

/*==============================================================*/
/* Table: dictionaryItem                                        */
/*==============================================================*/
create table dictionaryItem
(
   id                   int not null auto_increment  comment '',
   dictId               int not null  comment '',
   code                 varchar(50) not null  comment '',
   `value`              varchar(50) not null  comment '',
   sort                 int not null  comment '',
   primary key (id)
);

/*==============================================================*/
/* Table: organization                                          */
/*==============================================================*/
create table organization
(
   id                   int not null auto_increment  comment '',
   pid                  int not null  comment '',
   code                 varchar(50) not null  comment '',
   pCode                varchar(266) not null  comment '',
   name                 varchar(50) not null  comment '',
   type                 tinyint not null  comment '直线制；职能制；直线－职能制；事业部制；模拟分权制；矩阵制；扁平式',
   sort                 int not null  comment '',
   leaf                 bool not null  comment '',
   enabled              bool not null  comment '',
   createdDate          timestamp not null  comment '',
   createdBy            varchar(20) not null  comment '',
   lastModifiedDate     timestamp not null  comment '',
   lastModifiedBy       varchar(20) not null  comment '',
   version              int not null  comment '',
   description          varchar(500)  comment '',
   functionalAuthority  varchar(1000)  comment '',
   workingProcedure     varchar(1000)  comment '',
   relatedRequirement   varchar(1000)  comment '',
   primary key (id)
);

/*==============================================================*/
/* Table: permission                                            */
/*==============================================================*/
create table permission
(
   id                   int not null auto_increment  comment '',
   pid                  int not null  comment '',
   code                 varchar(50) not null  comment '',
   pCode                varchar(266) not null  comment '',
   name                 varchar(50) not null  comment '',
   type                 tinyint not null  comment '模块；菜单；功能',
   sort                 int not null  comment '',
   leaf                 bool not null  comment '',
   enabled              bool not null  comment '',
   createdDate          timestamp not null  comment '',
   createdBy            varchar(20) not null  comment '',
   lastModifiedDate     timestamp not null  comment '',
   lastModifiedBy       varchar(20) not null  comment '',
   version              int not null  comment '',
   description          varchar(500)  comment '',
   primary key (id)
);

/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table role
(
   id                   int not null auto_increment  comment '',
   code                 varchar(50) not null  comment '',
   name                 varchar(50) not null  comment '',
   enabled              bool not null  comment '',
   createdDate          timestamp not null  comment '',
   createdBy            varchar(20) not null  comment '',
   lastModifiedDate     timestamp not null  comment '',
   lastModifiedBy       varchar(20) not null  comment '',
   version              int not null  comment '',
   description          varchar(500)  comment '',
   primary key (id)
);

/*==============================================================*/
/* Table: rolePermission                                        */
/*==============================================================*/
create table rolePermission
(
   roleId               int not null  comment '',
   permId               int not null  comment '',
   primary key (roleId, permId)
);

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table `user`
(
   id                   int not null auto_increment  comment '',
   subject              varchar(20) not null  comment '',
   password             char(60) not null  comment '',
   pwdChangeDate        date not null  comment '',
   enabled              bool not null  comment '',
   createdDate          timestamp not null  comment '',
   createdBy            varchar(20) not null  comment '',
   lastModifiedDate     timestamp not null  comment '',
   lastModifiedBy       varchar(20) not null  comment '',
   version              int not null  comment '',
   unlockingDate        timestamp  comment '',
   disabledDate         timestamp  comment '',
   username             varchar(20)  comment '',
   nickname             varchar(20)  comment '',
   picture              varchar(50)  comment '',
   phoneNumber          varchar(20)  comment '',
   phoneNumberVerified  bool  comment '',
   email                varchar(50)  comment '',
   emailVerified        bool  comment '',
   gender               tinyint  comment '',
   birthdate            date  comment '',
   address              varchar(100)  comment '',
   description          varchar(500)  comment '',
   roleId               int  comment '',
   orgId                int  comment '',
   primary key (id)
);
