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
import com.badlogic.gdx.utils.FloatArray;
import com.mbrlabs.mundus.commons.terrain.SplatMap;
import com.mbrlabs.mundus.commons.terrain.SplatTexture;
import com.mbrlabs.mundus.commons.terrain.Terrain;
import com.mbrlabs.mundus.commons.terrain.TerrainTexture;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * @author Marcus Brummer
 * @version 01-10-2016
 */
public class TerrainAsset extends Asset {

    private float[] data;

    // dependencies
    private PixmapTextureAsset splatmap;
    private TextureAsset splatBase;
    private TextureAsset splatR;
    private TextureAsset splatG;
    private TextureAsset splatB;
    private TextureAsset splatA;

    private Terrain terrain;

    public TerrainAsset(MetaFile meta, FileHandle assetFile) {
        super(meta, assetFile);
    }

    public float[] getData() {
        return data;
    }

    public PixmapTextureAsset getSplatmap() {
        return splatmap;
    }

    public void setSplatmap(PixmapTextureAsset splatmap) {
        this.splatmap = splatmap;
        if (splatmap == null) {
            getMeta().setTerrainSplatmap(null);
        } else {
            getMeta().setTerrainSplatmap(splatmap.getUUID());
        }
    }

    public TextureAsset getSplatBase() {
        return splatBase;
    }

    public void setSplatBase(TextureAsset splatBase) {
        this.splatBase = splatBase;
        if (splatBase == null) {
            getMeta().setTerrainSplatBase(null);
        } else {
            getMeta().setTerrainSplatBase(splatBase.getUUID());
        }
    }

    public TextureAsset getSplatR() {
        return splatR;
    }

    public void setSplatR(TextureAsset splatR) {
        this.splatR = splatR;
        if (splatR == null) {
            getMeta().setTerrainSplatR(null);
        } else {
            getMeta().setTerrainSplatR(splatR.getUUID());
        }
    }

    public TextureAsset getSplatG() {
        return splatG;
    }

    public void setSplatG(TextureAsset splatG) {
        this.splatG = splatG;
        if (splatG == null) {
            getMeta().setTerrainSplatG(null);
        } else {
            getMeta().setTerrainSplatG(splatG.getUUID());
        }
    }

    public TextureAsset getSplatB() {
        return splatB;
    }

    public void setSplatB(TextureAsset splatB) {
        this.splatB = splatB;
        if (splatB == null) {
            getMeta().setTerrainSplatB(null);
        } else {
            getMeta().setTerrainSplatB(splatB.getUUID());
        }
    }

    public TextureAsset getSplatA() {
        return splatA;
    }

    public void setSplatA(TextureAsset splatA) {
        this.splatA = splatA;
        if (splatA == null) {
            getMeta().setTerrainSplatA(null);
        } else {
            getMeta().setTerrainSplatA(splatA.getUUID());
        }
    }

    public Terrain getTerrain() {
        return terrain;
    }

    @Override
    public void load() {
        // load height data from terra file
        final FloatArray floatArray = new FloatArray();

        DataInputStream is;
        try {
            is = new DataInputStream(new BufferedInputStream(new GZIPInputStream(file.read())));
            while (is.available() > 0) {
                floatArray.add(is.readFloat());
            }
            is.close();
        } catch (EOFException e) {
            // e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        data = floatArray.toArray();

        terrain = new Terrain(getMeta().getTerrainSize(), data);
        terrain.init();
        terrain.update();
    }

    @Override
    public void applyDependencies() {
        TerrainTexture terrainTexture = terrain.getTerrainTexture();

        if (splatmap == null) {
            terrainTexture.setSplatmap(null);
        } else {
            terrainTexture.setSplatmap(new SplatMap(splatmap));
        }
        if (splatBase == null) {
            terrainTexture.removeTexture(SplatTexture.Channel.BASE);
        } else {
            terrainTexture.setSplatTexture(new SplatTexture(SplatTexture.Channel.BASE, splatBase));
        }
        if (splatR == null) {
            terrainTexture.removeTexture(SplatTexture.Channel.R);
        } else {
            terrainTexture.setSplatTexture(new SplatTexture(SplatTexture.Channel.R, splatR));
        }
        if (splatG == null) {
            terrainTexture.removeTexture(SplatTexture.Channel.G);
        } else {
            terrainTexture.setSplatTexture(new SplatTexture(SplatTexture.Channel.G, splatG));
        }
        if (splatB == null) {
            terrainTexture.removeTexture(SplatTexture.Channel.B);
        } else {
            terrainTexture.setSplatTexture(new SplatTexture(SplatTexture.Channel.B, splatB));
        }
        if (splatA == null) {
            terrainTexture.removeTexture(SplatTexture.Channel.A);
        } else {
            terrainTexture.setSplatTexture(new SplatTexture(SplatTexture.Channel.A, splatA));
        }

        terrain.update();
    }

    @Override
    public void dispose() {

    }
}
