-- liquibase formatted sql

-- changeset macxan:1683369970475-1
CREATE TABLE "city" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, "available" BOOLEAN NOT NULL, "name" VARCHAR(255), "created_at" TIMESTAMP WITHOUT TIME ZONE, "update_at" TIMESTAMP WITHOUT TIME ZONE, "weather_id" BIGINT, CONSTRAINT "city_pkey" PRIMARY KEY ("id"));

-- changeset macxan:1683369970475-2
CREATE TABLE "users" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, "account_non_expired" BOOLEAN NOT NULL, "account_non_locked" BOOLEAN NOT NULL, "credentials_non_expired" BOOLEAN NOT NULL, "enabled" BOOLEAN NOT NULL, "password" VARCHAR(255) NOT NULL, "role" VARCHAR(255), "username" VARCHAR(255) NOT NULL, "created_at" TIMESTAMP WITHOUT TIME ZONE, "update_at" TIMESTAMP WITHOUT TIME ZONE, CONSTRAINT "users_pkey" PRIMARY KEY ("id"));

-- changeset macxan:1683369970475-3
CREATE TABLE "users_subscribed_cities" ("users_id" BIGINT NOT NULL, "subscribed_cities_id" BIGINT NOT NULL, CONSTRAINT "users_subscribed_cities_pkey" PRIMARY KEY ("users_id", "subscribed_cities_id"));

-- changeset macxan:1683369970475-4
CREATE TABLE "weather" ("id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL, "created_at" TIMESTAMP WITHOUT TIME ZONE, "humidity" INTEGER, "pressure" INTEGER, "temp" FLOAT4, "update_at" TIMESTAMP WITHOUT TIME ZONE, "wind_speed" FLOAT4, CONSTRAINT "weather_pkey" PRIMARY KEY ("id"));

-- changeset macxan:1683369970475-5
ALTER TABLE "users_subscribed_cities" ADD CONSTRAINT "uk_29gpfc891ije88ehi2t8sk40g" UNIQUE ("subscribed_cities_id");

-- changeset macxan:1683369970475-6
ALTER TABLE "users" ADD CONSTRAINT "uk_r43af9ap4edm43mmtq01oddj6" UNIQUE ("username");

-- changeset macxan:1683369970475-7
ALTER TABLE "users_subscribed_cities" ADD CONSTRAINT "fk2wvrpqahr7q1wkj5l5whigmcf" FOREIGN KEY ("users_id") REFERENCES "users" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset macxan:1683369970475-8
ALTER TABLE "city" ADD CONSTRAINT "fkffah0d8lnfexw9b0ssltrg8gr" FOREIGN KEY ("weather_id") REFERENCES "weather" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset macxan:1683369970475-9
ALTER TABLE "users_subscribed_cities" ADD CONSTRAINT "fkj9o9os3qsdi8ereue8xp1scy" FOREIGN KEY ("subscribed_cities_id") REFERENCES "city" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

