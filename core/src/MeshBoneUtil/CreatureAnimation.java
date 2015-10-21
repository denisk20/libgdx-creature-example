package MeshBoneUtil;

import com.badlogic.gdx.utils.JsonValue;

/******************************************************************************
 * Creature Runtimes License
 * <p/>
 * Copyright (c) 2015, Kestrel Moon Studios
 * All rights reserved.
 * <p/>
 * Preamble: This Agreement governs the relationship between Licensee and Kestrel Moon Studios(Hereinafter: Licensor).
 * This Agreement sets the terms, rights, restrictions and obligations on using [Creature Runtimes] (hereinafter: The Software) created and owned by Licensor,
 * as detailed herein:
 * License Grant: Licensor hereby grants Licensee a Sublicensable, Non-assignable & non-transferable, Commercial, Royalty free,
 * Including the rights to create but not distribute derivative works, Non-exclusive license, all with accordance with the terms set forth and
 * other legal restrictions set forth in 3rd party software used while running Software.
 * Limited: Licensee may use Software for the purpose of:
 * Running Software on Licensee’s Website[s] and Server[s];
 * Allowing 3rd Parties to run Software on Licensee’s Website[s] and Server[s];
 * Publishing Software’s output to Licensee and 3rd Parties;
 * Distribute verbatim copies of Software’s output (including compiled binaries);
 * Modify Software to suit Licensee’s needs and specifications.
 * Binary Restricted: Licensee may sublicense Software as a part of a larger work containing more than Software,
 * distributed solely in Object or Binary form under a personal, non-sublicensable, limited license. Such redistribution shall be limited to unlimited codebases.
 * Non Assignable & Non-Transferable: Licensee may not assign or transfer his rights and duties under this license.
 * Commercial, Royalty Free: Licensee may use Software for any purpose, including paid-services, without any royalties
 * Including the Right to Create Derivative Works: Licensee may create derivative works based on Software,
 * including amending Software’s source code, modifying it, integrating it into a larger work or removing portions of Software,
 * as long as no distribution of the derivative works is made
 * <p/>
 * THE RUNTIMES IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE RUNTIMES OR THE USE OR OTHER DEALINGS IN THE
 * RUNTIMES.
 *****************************************************************************/

// Class for animating the creature character
public class CreatureAnimation {
    public String name;
    public float start_time, end_time;
    public MeshBoneCacheManager bones_cache;
    public MeshDisplacementCacheManager displacement_cache;
    public MeshUVWarpCacheManager uv_warp_cache;

    public CreatureAnimation(JsonValue load_data,
                             String name_in) {
        name = name_in;
        bones_cache = new MeshBoneCacheManager();
        displacement_cache = new MeshDisplacementCacheManager();
        uv_warp_cache = new MeshUVWarpCacheManager();

        LoadFromData(name_in, load_data);
    }

    public void LoadFromData(String name_in,
                             JsonValue load_data) {
        JsonValue json_anim_base = load_data.get("animation");
        JsonValue json_clip = json_anim_base.get(name_in);

        Tuple<Integer, Integer> start_end_times = CreatureModuleUtils.GetStartEndTimes(json_clip, "bones");
        start_time = start_end_times.x;
        end_time = start_end_times.y;

        // bone animation
        CreatureModuleUtils.FillBoneCache(json_clip,
                "bones",
                (int) start_time,
                (int) end_time,
                bones_cache);

        // mesh deformation animation
        CreatureModuleUtils.FillDeformationCache(json_clip,
                "meshes",
                (int) start_time,
                (int) end_time,
                displacement_cache);

        // uv swapping animation
        CreatureModuleUtils.FillUVSwapCache(json_clip,
                "uv_swaps",
                (int) start_time,
                (int) end_time,
                uv_warp_cache);
    }
}