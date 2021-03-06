/*
 * Copyright (c) 2016. See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mbrlabs.mundus.commons.assets;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Meta files contain additional information about a single asset file.
 *
 * @author Marcus Brummer
 * @version 01-10-2016
 */
public class MetaFile {

    private static final String TAG = MetaFile.class.getSimpleName();

    public static final String META_EXTENSION = "meta";
    public static final int CURRENT_VERSION = 1;

    private static final String COMMENT = "# !!! WARNING, DO NOT MODIFY OR DELETE !!! \n "
            + "This file is machine generated. If you delete or modify this, Mundus might not work anymore.\n";

    private static final String PROP_VERSION = "version";
    private static final String PROP_UUID = "uuid";
    private static final String PROP_LAST_MODIFIED = "last_modified";
    private static final String PROP_TYPE = "type";

    // model material
    private static final String PROP_MATERIAL_DIFFUSE_COLOR = "mat.diffuse.color";
    private static final String PROP_MATERIAL_DIFFUSE_TEXTURE = "mat.diffuse.texture";

    // terrain
    private static final String PROP_TERRAIN_SIZE = "terrain.size";
    private static final String PROP_MATERIAL_TERRAIN_SPLATMAP = "terrain.mat.splatmap";
    private static final String PROP_MATERIAL_TERRAIN_SPLAT_BASE = "terrain.mat.splat.base";
    private static final String PROP_MATERIAL_TERRAIN_SPLAT_R = "terrain.mat.splat.r";
    private static final String PROP_MATERIAL_TERRAIN_SPLAT_G = "terrain.mat.splat.g";
    private static final String PROP_MATERIAL_TERRAIN_SPLAT_B = "terrain.mat.splat.b";
    private static final String PROP_MATERIAL_TERRAIN_SPLAT_A = "terrain.mat.splat.a";

    private FileHandle file;
    private Properties props;

    // general stuff
    private int version;
    private AssetType type;
    private String uuid;
    private Date lastModified;

    // model specific
    private Color diffuseColor = null;
    private String diffuseTexture = null;

    // terrain specific
    private String terrainSplatmap;
    private String terrainSplatBase;
    private String terrainSplatR;
    private String terrainSplatG;
    private String terrainSplatB;
    private String terrainSplatA;
    private int terrainSize;

    public MetaFile(FileHandle file) {
        this.file = file;
        this.props = new Properties();
    }

    // TODO move this out into a manager class
    public void save() throws IOException {
        props.clear();
        props.setProperty(PROP_VERSION, String.valueOf(this.version));
        props.setProperty(PROP_TYPE, this.type.name());
        props.setProperty(PROP_UUID, this.uuid);
        props.setProperty(PROP_LAST_MODIFIED, String.valueOf(this.lastModified.getTime()));

        // model specific
        if (type == AssetType.MODEL) {
            if (diffuseColor != null) {
                props.setProperty(PROP_MATERIAL_DIFFUSE_COLOR, diffuseColor.toString());
            }
            if (diffuseTexture != null) {
                props.setProperty(PROP_MATERIAL_DIFFUSE_TEXTURE, diffuseTexture);
            }
        }

        // terrain specific
        if (type == AssetType.TERRAIN) {
            if (terrainSplatmap != null) {
                props.setProperty(PROP_MATERIAL_TERRAIN_SPLATMAP, terrainSplatmap);
            }
            if (terrainSplatBase != null) {
                props.setProperty(PROP_MATERIAL_TERRAIN_SPLAT_BASE, terrainSplatBase);
            }
            if (terrainSplatR != null) {
                props.setProperty(PROP_MATERIAL_TERRAIN_SPLAT_R, terrainSplatR);
            }
            if (terrainSplatG != null) {
                props.setProperty(PROP_MATERIAL_TERRAIN_SPLAT_G, terrainSplatG);
            }
            if (terrainSplatB != null) {
                props.setProperty(PROP_MATERIAL_TERRAIN_SPLAT_B, terrainSplatB);
            }
            if (terrainSplatA != null) {
                props.setProperty(PROP_MATERIAL_TERRAIN_SPLAT_A, terrainSplatA);
            }
            props.setProperty(PROP_TERRAIN_SIZE, String.valueOf(terrainSize));
        }

        props.store(new FileOutputStream(file.file()), COMMENT);
    }

    // TODO move this out into a manager class
    public void load() throws MetaFileParseException {
        try {
            props.clear();
            props.load(new FileInputStream(file.file()));

            this.version = Integer.valueOf(props.getProperty(PROP_VERSION));
            this.type = AssetType.valueOf(props.getProperty(PROP_TYPE));
            this.uuid = props.getProperty(PROP_UUID);
            this.lastModified = new Date(Long.valueOf(props.getProperty(PROP_LAST_MODIFIED)));

            // model specific
            if (type == AssetType.MODEL) {
                String color = props.getProperty(PROP_MATERIAL_DIFFUSE_COLOR, null);
                if (color != null) {
                    this.diffuseColor = Color.valueOf(color);
                }
                this.diffuseTexture = props.getProperty(PROP_MATERIAL_DIFFUSE_TEXTURE, null);
            }

            // terrain specific
            if (type == AssetType.TERRAIN) {
                this.terrainSplatmap = props.getProperty(PROP_MATERIAL_TERRAIN_SPLATMAP, null);
                this.terrainSplatBase = props.getProperty(PROP_MATERIAL_TERRAIN_SPLAT_BASE, null);
                this.terrainSplatR = props.getProperty(PROP_MATERIAL_TERRAIN_SPLAT_R, null);
                this.terrainSplatG = props.getProperty(PROP_MATERIAL_TERRAIN_SPLAT_G, null);
                this.terrainSplatB = props.getProperty(PROP_MATERIAL_TERRAIN_SPLAT_B, null);
                this.terrainSplatA = props.getProperty(PROP_MATERIAL_TERRAIN_SPLAT_A, null);
                this.terrainSize = Integer.valueOf(props.getProperty(PROP_TERRAIN_SIZE, "1200"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new MetaFileParseException();
        }
    }

    public String getDiffuseTexture() {
        return diffuseTexture;
    }

    public void setDiffuseTexture(String diffuseTexture) {
        this.diffuseTexture = diffuseTexture;
    }

    public Color getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Color diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getID() {
        return uuid;
    }

    public void setID(String id) {
        this.uuid = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public AssetType getType() {
        return type;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getTerrainSplatA() {
        return terrainSplatA;
    }

    public void setTerrainSplatA(String terrainSplatA) {
        this.terrainSplatA = terrainSplatA;
    }

    public String getTerrainSplatB() {
        return terrainSplatB;
    }

    public void setTerrainSplatB(String terrainSplatB) {
        this.terrainSplatB = terrainSplatB;
    }

    public String getTerrainSplatG() {
        return terrainSplatG;
    }

    public void setTerrainSplatG(String terrainSplatG) {
        this.terrainSplatG = terrainSplatG;
    }

    public String getTerrainSplatR() {
        return terrainSplatR;
    }

    public void setTerrainSplatR(String terrainSplatR) {
        this.terrainSplatR = terrainSplatR;
    }

    public String getTerrainSplatBase() {
        return terrainSplatBase;
    }

    public void setTerrainSplatBase(String terrainSplatBase) {
        this.terrainSplatBase = terrainSplatBase;
    }

    public String getTerrainSplatmap() {
        return terrainSplatmap;
    }

    public void setTerrainSplatmap(String terrainSplatmap) {
        this.terrainSplatmap = terrainSplatmap;
    }

    public int getTerrainSize() {
        return terrainSize;
    }

    public void setTerrainSize(int terrainSize) {
        this.terrainSize = terrainSize;
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    public FileHandle getFile() {
        return file;
    }

}
