
	drop table if exists Ad

    drop table if exists Annal

    drop table if exists Calculator

    drop table if exists Cart

    drop table if exists Gamester

    drop table if exists Goods

    drop table if exists Issue

    drop table if exists Lottery

    drop table if exists Lucky

    drop table if exists Number

    drop table if exists Picture

    create table Ad (
        id integer not null auto_increment,
        image varchar(255),
        primary key (id)
    )

    create table Annal (
        id integer not null auto_increment,
        num bigint not null,
        time varchar(255),
        gamester_id integer not null,
        goods_id integer not null,
        issue_id integer not null,
        primary key (id)
    )

    create table Calculator (
        id integer not null auto_increment,
        numA bigint not null,
        numB bigint not null,
        result bigint not null,
        goods_id integer not null,
        issue_id integer not null,
        primary key (id),
        unique (goods_id),
        unique (issue_id)
    )

    create table Cart (
        id integer not null auto_increment,
        num integer not null,
        status integer not null,
        total integer not null,
        gamester_id integer not null,
        goods_id integer not null,
        issue_id integer not null,
        primary key (id),
        unique (gamester_id),
        unique (goods_id),
        unique (issue_id)
    )

    create table Gamester (
        id integer not null auto_increment,
        account varchar(255),
        city varchar(255),
        ip varchar(255),
        money integer not null,
        nickname varchar(255),
        password varchar(255),
        phone varchar(255),
        portrait varchar(255),
        power integer not null,
        stone integer not null,
        time varchar(255),
        tip varchar(255),
        primary key (id)
    )

    create table Goods (
        id integer not null auto_increment,
        available varchar(255),
        cate varchar(255),
        explains TEXT,
        image varchar(255),
        intro TEXT,
        per integer not null,
        price integer not null,
        remind TEXT,
        retime varchar(255),
        time varchar(255),
        total integer not null,
        trait varchar(255),
        primary key (id)
    )

    create table Issue (
        id integer not null auto_increment,
        done integer not null,
        finish varchar(10),
        over varchar(255),
        start varchar(30),
        goods_id integer,
        primary key (id)
    )

    create table Lottery (
        id integer not null auto_increment,
        code varchar(255),
        error varchar(255),
        expect varchar(255),
        stamp varchar(255),
        time varchar(255),
        primary key (id)
    )

    create table Lucky (
        id integer not null auto_increment,
        time varchar(255),
        gamester_id integer not null,
        goods_id integer not null,
        issue_id integer not null,
        primary key (id),
        unique (issue_id)
    )

    create table Number (
        id integer not null auto_increment,
        num bigint not null,
        goods_id integer not null,
        issue_id integer not null,
        primary key (id)
    )

    create table Picture (
        id integer not null auto_increment,
        image varchar(255),
        type integer not null,
        goods_id integer not null,
        primary key (id)
    )

    alter table Annal 
        add index FK3C7A1AC7AB0C4F3 (gamester_id), 
        add constraint FK3C7A1AC7AB0C4F3 
        foreign key (gamester_id) 
        references Gamester (id)

    alter table Annal 
        add index FK3C7A1AC7071F161 (goods_id), 
        add constraint FK3C7A1AC7071F161 
        foreign key (goods_id) 
        references Goods (id)

    alter table Annal 
        add index FK3C7A1AC1BA7DE81 (issue_id), 
        add constraint FK3C7A1AC1BA7DE81 
        foreign key (issue_id) 
        references Issue (id)

    alter table Calculator 
        add index FK32F547227071F161 (goods_id), 
        add constraint FK32F547227071F161 
        foreign key (goods_id) 
        references Goods (id)

    alter table Calculator 
        add index FK32F547221BA7DE81 (issue_id), 
        add constraint FK32F547221BA7DE81 
        foreign key (issue_id) 
        references Issue (id)

    alter table Cart 
        add index FK1FEF407AB0C4F3 (gamester_id), 
        add constraint FK1FEF407AB0C4F3 
        foreign key (gamester_id) 
        references Gamester (id)

    alter table Cart 
        add index FK1FEF407071F161 (goods_id), 
        add constraint FK1FEF407071F161 
        foreign key (goods_id) 
        references Goods (id)

    alter table Cart 
        add index FK1FEF401BA7DE81 (issue_id), 
        add constraint FK1FEF401BA7DE81 
        foreign key (issue_id) 
        references Issue (id)

    alter table Issue 
        add index FK43AB8B97071F161 (goods_id), 
        add constraint FK43AB8B97071F161 
        foreign key (goods_id) 
        references Goods (id)

    alter table Lucky 
        add index FK465AAC87AB0C4F3 (gamester_id), 
        add constraint FK465AAC87AB0C4F3 
        foreign key (gamester_id) 
        references Gamester (id)

    alter table Lucky 
        add index FK465AAC87071F161 (goods_id), 
        add constraint FK465AAC87071F161 
        foreign key (goods_id) 
        references Goods (id)

    alter table Lucky 
        add index FK465AAC81BA7DE81 (issue_id), 
        add constraint FK465AAC81BA7DE81 
        foreign key (issue_id) 
        references Issue (id)

    alter table Number 
        add index FK8BBDC7697071F161 (goods_id), 
        add constraint FK8BBDC7697071F161 
        foreign key (goods_id) 
        references Goods (id)

    alter table Number 
        add index FK8BBDC7691BA7DE81 (issue_id), 
        add constraint FK8BBDC7691BA7DE81 
        foreign key (issue_id) 
        references Issue (id)

    alter table Picture 
        add index FK40C8F4DE7071F161 (goods_id), 
        add constraint FK40C8F4DE7071F161 
        foreign key (goods_id) 
        references Goods (id)
