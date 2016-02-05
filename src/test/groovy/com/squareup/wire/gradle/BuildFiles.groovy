package com.squareup.wire.gradle

import groovy.transform.CompileStatic
import org.junit.rules.TemporaryFolder

import static org.assertj.core.api.Assertions.assertThat

@CompileStatic
class BuildFiles {
  static final String PACKAGE = "arbitrary.string"

  static File makeHierarchy(TemporaryFolder folder, String... foldersAndFiles) {
    assertThat(foldersAndFiles.length).isGreaterThanOrEqualTo(2)

    final file = new File(folder.root, foldersAndFiles.join("/"))
    file.parentFile.mkdirs()
    file
  }

  static String manifest() {
    """<?xml version="1.0" encoding="utf-8"?>
<manifest package="${PACKAGE}">
    <application />
</manifest>
"""
  }

  static String protoFile() {
    """
syntax = "proto2";

package ${PACKAGE};

enum AttachmentMType {
  audio = 1;
  video = 2;
  gift = 3;
  doc = 4;
  photo = 5;
  wall = 6;
  wall_reply = 7;
  sticker = 8;
  link = 9;
}

message Audio {
  required int64 id = 1;
  required sint64 owner_id = 2;
  required string artist = 3;
  required string title = 4;
  required int32 duration = 5;
  optional int64 lyrics_id = 6;
  optional int64 album_id = 7;
  optional int32 genre_id = 8;
  optional string url = 9;
  optional int32 no_search = 10;
}

message Video {
  required int64 id = 1;
  required sint64 owner_id = 2;
  required string title = 3;
  required int32 duration = 4;
  required string description = 5;
  required int64 date = 6;
  required int32 views = 7;
  required int32 comments = 8;
  required string photo_130 = 9;
  required string photo_320 = 10;
  optional string photo_640 = 11;
  optional string photo_800 = 12;
  required string access_key = 13;
  optional int32 is_private = 14;
}

message Gift {
  required int64 id = 1;
  required string thumb_48 = 2;
  required string thumb_96 = 3;
  required string thumb_256 = 4;
}

message Doc {
  required int64 id = 1;
  required sint64 owner_id = 2;
  required string title = 3;
  required int64 size = 4;
  required string ext = 5;
  required string url = 6;
  optional string photo_100 = 7;
  optional string photo_130 = 8;
  required string access_key = 9;
}

message Photo {
  required int64 id = 1;
  required int64 album_id = 2;
  required sint64 owner_id = 3;
  optional int32 width = 4;
  optional int32 height = 5;
  required string text = 6;
  required int64 date = 7;
  required string photo_75 = 8;
  required string photo_130 = 9;
  required string photo_604 = 10;
  optional string photo_807 = 11;
  optional string photo_1280 = 12;
  optional string photo_2560 = 13;
  optional string access_key = 14;
  optional int64 user_id = 15;
  optional int64 post_id = 16;
  optional float lat = 17;
  optional float lng = 18;
}

message Sticker {
  required int64 id = 1;
  required int64 product_id = 2;
  required string photo_64 = 3;
  required string photo_128 = 4;
  required string photo_256 = 5;
  required string photo_352 = 6;
  required int32 width = 7;
  required int32 height = 8;
}

message Link {
  required string url = 1;
  required string title = 2;
  required string description = 3;
  optional string image_src = 4;
  optional string preview_page = 5;
  optional string preview_url = 6;
}
"""
  }
}
