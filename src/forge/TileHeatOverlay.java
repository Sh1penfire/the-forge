package forge;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Interp;
import arc.math.Mathf;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.graphics.Pal;

import static forge.ForgeMain.heat;
import static forge.HeatControl.enabled;
import static forge.HeatControl.kelvins;

public class TileHeatOverlay {
    public void draw(){

        for (HeatControl.Chunk chunk: HeatControl.gridChunks) {
            if (chunk.state.enabled) Draw.color(Color.white);
            else Draw.color(Color.red, Pal.remove, Mathf.absin(30, 1));
            Lines.stroke(0.5f + (enabled ? Mathf.absin(1.5f, 1) : 0.5f));
            Lines.rect(chunk.x * Vars.tilesize, chunk.y * Vars.tilesize, HeatControl.chunkSize * Vars.tilesize, HeatControl.chunkSize * Vars.tilesize);
        }

        for (int i = 0; i < heat.s; i++) {
            Tmp.v1.set((i % heat.width) * Vars.tilesize, (i / heat.width) * Vars.tilesize);
            Core.camera.bounds(Tmp.r1);
            if(!Tmp.r2.setCentered(Tmp.v1.x, Tmp.v1.y, Vars.tilesize).overlaps(Tmp.r1)) continue;

            float temp = HeatControl.gridTiles[i].top().temperature;

            /*
            Ranges for colors
            Blue: 0 - 303.15
            Green: 0 - 573.15
            Red: 313.15 - 1273.15
             */

            float b = Math.max(1 - temp/303.15f, 0);
            float g = Interp.Pow.slope.apply(Math.max(1 - temp/573.15f, 0));
            float r = Math.max((temp-313.15f)/1273.15f, 0);
            Draw.color(Tmp.c1.set(r, g, b, Math.min(0.15f + Interp.Pow.pow2.apply(Math.max(temp - 1273.15f, 0)/100000), 0.65f)));
            Fill.square(Tmp.v1.x, Tmp.v1.y, 4);
        }
    }
}