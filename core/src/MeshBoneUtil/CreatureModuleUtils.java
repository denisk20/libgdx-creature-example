package MeshBoneUtil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

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

public class CreatureModuleUtils {
    public static JsonValue LoadCreatureJSONData(String filename_in) {
        FileHandle file = Gdx.files.internal(filename_in);
        String text = file.readString();

        JsonValue ret_root = null;
        ret_root = new JsonReader().parse(text);

        return ret_root;
    }

    public static Vector<String> GetAllAnimationNames(JsonValue json_data) {
        JsonValue json_animations = json_data.get("animation");
        Vector<String> keys = new Vector<String>();
        for (JsonValue entry = json_animations.child; entry != null; entry = entry.next) {
            keys.add(entry.name);
        }

        return keys;
    }

    public static float[] getFloatArray(JsonValue raw_data) {
        return raw_data.asFloatArray();
    }

    public static int[] getIntArray(JsonValue raw_data) {
        return raw_data.asIntArray();
    }


    public static Vector<Vector2> ReadPointsArray2DJSON(JsonValue data,
                                                        String key) {
        float[] raw_array = getFloatArray(data.get(key));
        Vector<Vector2> ret_list = new Vector<Vector2>();
        int num_points = raw_array.length / 2;
        for (int i = 0; i < num_points; i++) {
            int cur_index = i * 2;
            ret_list.add(
                    new Vector2(raw_array[0 + cur_index], raw_array[1 + cur_index]));
        }

        return ret_list;
    }

    public static Vector<Float> ReadFloatArray3DJSON(JsonValue data,
                                                     String key) {
        float[] raw_array = getFloatArray(data.get(key));

        Vector<Float> ret_list = new Vector<Float>();
        int num_points = raw_array.length / 2;
        for (int i = 0; i < num_points; i++) {
            int cur_index = i * 2;
            ret_list.add((float) raw_array[0 + cur_index]);
            ret_list.add((float) raw_array[1 + cur_index]);
            ret_list.add((float) 0);
        }

        return ret_list;
    }

    public static boolean ReadBoolJSON(JsonValue data,
                                       String key) {
        boolean val = (boolean) data.get(key).asBoolean();
        return val;
    }

    public static Vector<Float> ReadFloatArrayJSON(JsonValue data,
                                                   String key) {
        float[] raw_array = getFloatArray(data.get(key));
        Vector<Float> ret_list = new Vector<Float>();
        for (int i = 0; i < raw_array.length; i++) {
            ret_list.add(raw_array[i]);
        }

        return ret_list;
    }

    public static Vector<Integer> ReadIntArrayJSON(JsonValue data,
                                                   String key) {
        int[] raw_array = getIntArray(data.get(key));
        Vector<Integer> ret_list = new Vector<Integer>();

        for (int i = 0; i < raw_array.length; i++) {
            ret_list.add(raw_array[i]);
        }

        return ret_list;
    }

    public static Matrix4 ReadMatrixJSON(JsonValue data,
                                         String key) {
        float[] raw_array = getFloatArray(data.get(key));
        return new Matrix4(raw_array);
    }

    public static Vector2 ReadVector2JSON(JsonValue data,
                                          String key) {
        float[] raw_array = getFloatArray(data.get(key));
        return new Vector2(raw_array[0], raw_array[1]);
    }


    public static Vector3 ReadVector3JSON(JsonValue data,
                                          String key) {
        float[] raw_array = getFloatArray(data.get(key));
        return new Vector3(raw_array[0], raw_array[1], 0);
    }

    public static MeshBone CreateBones(JsonValue json_obj,
                                       String key) {
        MeshBone root_bone = null;
        JsonValue base_obj = json_obj.get(key);
        HashMap<Integer, Tuple<MeshBone, Vector<Integer>>> bone_data = new HashMap<Integer, Tuple<MeshBone, Vector<Integer>>>();
        HashSet<Integer> child_set = new HashSet<Integer>();

        // layout bones
        for (JsonValue cur_node = base_obj.child; cur_node != null; cur_node = cur_node.next) {

            String cur_name = cur_node.name;

            int cur_id = (int) cur_node.get("id").asInt(); //GetJSONNodeFromKey(*cur_node, "id")->value.toNumber();
            Matrix4 cur_parent_mat = ReadMatrixJSON(cur_node, "restParentMat");

            Vector3 cur_local_rest_start_pt = ReadVector3JSON(cur_node, "localRestStartPt");
            Vector3 cur_local_rest_end_pt = ReadVector3JSON(cur_node, "localRestEndPt");
            Vector<Integer> cur_children_ids = ReadIntArrayJSON(cur_node, "children");

            MeshBone new_bone = new MeshBone(cur_name,
                    new Vector3(0, 0, 0),
                    new Vector3(0, 0, 0),
                    cur_parent_mat);
            new_bone.local_rest_start_pt = cur_local_rest_start_pt;
            new_bone.local_rest_end_pt = cur_local_rest_end_pt;
            new_bone.calcRestData();
            new_bone.setTagId(cur_id);

            bone_data.put(cur_id, new Tuple<MeshBone, Vector<Integer>>(new_bone, cur_children_ids));

            for (int cur_child_id : cur_children_ids) {
                child_set.add(cur_child_id);
            }
        }

        // Find root
        for (Map.Entry<Integer, Tuple<MeshBone, Vector<Integer>>> cur_data : bone_data.entrySet()) {
            int cur_id = cur_data.getKey();
            if (child_set.contains(cur_id) == false) {
                // not a child, so is root
                root_bone = cur_data.getValue().x;
                break;
            }
        }

        // construct hierarchy
        for (Map.Entry<Integer, Tuple<MeshBone, Vector<Integer>>> cur_data : bone_data.entrySet()) {
            MeshBone cur_bone = cur_data.getValue().x;
            Vector<Integer> children_ids = cur_data.getValue().y;
            for (int cur_child_id : children_ids) {
                MeshBone child_bone = bone_data.get(cur_child_id).x;
                cur_bone.addChild(child_bone);
            }

        }


        return root_bone;
    }

    public static Vector<MeshRenderRegion> CreateRegions(JsonValue json_obj,
                                                         String key,
                                                         Vector<Integer> indices_in,
                                                         Vector<Float> rest_pts_in,
                                                         Vector<Float> uvs_in) {
        Vector<MeshRenderRegion> ret_regions = new Vector<MeshRenderRegion>();
        JsonValue base_obj = json_obj.get(key);

        for (JsonValue cur_node = base_obj.child; cur_node != null; cur_node = cur_node.next) {
            String cur_name = cur_node.name;

            int cur_id = (int) cur_node.get("id").asInt(); //(int)GetJSONNodeFromKey(*cur_node, "id")->value.toNumber();
            int cur_start_pt_index = (int) cur_node.get("start_pt_index").asInt(); //(int)GetJSONNodeFromKey(*cur_node, "start_pt_index")->value.toNumber();
            int cur_end_pt_index = (int) cur_node.get("end_pt_index").asInt(); //(int)GetJSONNodeFromKey(*cur_node, "end_pt_index")->value.toNumber();
            int cur_start_index = (int) cur_node.get("start_index").asInt(); //(int)GetJSONNodeFromKey(*cur_node, "start_index")->value.toNumber();
            int cur_end_index = (int) cur_node.get("end_index").asInt(); //(int)GetJSONNodeFromKey(*cur_node, "end_index")->value.toNumber();

            MeshRenderRegion new_region = new MeshRenderRegion(indices_in,
                    rest_pts_in,
                    uvs_in,
                    cur_start_pt_index,
                    cur_end_pt_index,
                    cur_start_index,
                    cur_end_index);

            new_region.setName(cur_name);
            new_region.setTagId(cur_id);

            // Read in weights
            HashMap<String, Vector<Float>> weight_map =
                    new_region.normal_weight_map;
            JsonValue weight_obj = cur_node.get("weights");

            for (JsonValue w_node = weight_obj.child; w_node != null; w_node = w_node.next) {
                String w_key = w_node.name;
                Vector<Float> values = ReadFloatArrayJSON(weight_obj, w_key);
                weight_map.put(w_key, values);
            }

            ret_regions.add(new_region);
        }

        return ret_regions;
    }

    public static Tuple<Integer, Integer> GetStartEndTimes(JsonValue json_obj,
                                                           String key) {
        int start_time = 0;
        int end_time = 0;
        boolean first = true;
        JsonValue base_obj = json_obj.get(key);

        for (JsonValue cur_node = base_obj.child; cur_node != null; cur_node = cur_node.next) {

            int cur_val = Integer.parseInt(cur_node.name);
            if (first) {
                start_time = cur_val;
                end_time = cur_val;
                first = false;
            } else {
                if (cur_val > end_time) {
                    end_time = cur_val;
                }
            }
        }

        MeshBoneUtil.Tuple<Integer, Integer> ret_times = new MeshBoneUtil.Tuple<Integer, Integer>(start_time, end_time);
        return ret_times;
    }

    static public void FillBoneCache(JsonValue json_obj,
                                     String key,
                                     int start_time,
                                     int end_time,
                                     MeshBoneCacheManager cache_manager) {
        JsonValue base_obj = json_obj.get(key);

        cache_manager.init(start_time, end_time);

        for (JsonValue cur_node = base_obj.child; cur_node != null; cur_node = cur_node.next) {
            int cur_time = Integer.parseInt(cur_node.name);

            Vector<MeshBoneCache> cache_list = new Vector<MeshBoneCache>();

            for (JsonValue bone_node = cur_node.child; bone_node != null; bone_node = bone_node.next) {
                String cur_name = bone_node.name;

                Vector3 cur_start_pt = ReadVector3JSON(bone_node, "start_pt"); //ReadJSONVec4_2(*bone_node, "start_pt");
                Vector3 cur_end_pt = ReadVector3JSON(bone_node, "end_pt"); //ReadJSONVec4_2(*bone_node, "end_pt");

                MeshBoneCache cache_data = new MeshBoneCache(cur_name);
                cache_data.setWorldStartPt(cur_start_pt);
                cache_data.setWorldEndPt(cur_end_pt);

                cache_list.add(cache_data);
            }

            int set_index = cache_manager.getIndexByTime(cur_time);
            cache_manager.bone_cache_table.set(set_index, cache_list);
        }

        cache_manager.makeAllReady();
    }

    public static void FillDeformationCache(JsonValue json_obj,
                                            String key,
                                            int start_time,
                                            int end_time,
                                            MeshDisplacementCacheManager cache_manager) {
        JsonValue base_obj = json_obj.get(key);

        cache_manager.init(start_time, end_time);

        for (JsonValue cur_node = base_obj.child; cur_node != null; cur_node = cur_node.next) {
            int cur_time = Integer.parseInt(cur_node.name);

            Vector<MeshDisplacementCache> cache_list = new Vector<MeshDisplacementCache>();

            for (JsonValue mesh_node = cur_node.child; mesh_node != null; mesh_node = mesh_node.next) {
                String cur_name = mesh_node.name;

                MeshDisplacementCache cache_data = new MeshDisplacementCache(cur_name);

                boolean use_local_displacement = ReadBoolJSON(mesh_node, "use_local_displacements"); //GetJSONNodeFromKey(*mesh_node, "use_local_displacements")->value.toBool();
                boolean use_post_displacement = ReadBoolJSON(mesh_node, "use_post_displacements"); //GetJSONNodeFromKey(*mesh_node, "use_post_displacements")->value.toBool();

                if (use_local_displacement) {
                    Vector<Vector2> read_pts = ReadPointsArray2DJSON(mesh_node,
                            "local_displacements"); //ReadJSONPoints2DVector(*mesh_node, "local_displacements");
                    cache_data.setLocalDisplacements(read_pts);
                }

                if (use_post_displacement) {
                    Vector<Vector2> read_pts = ReadPointsArray2DJSON(mesh_node,
                            "post_displacements"); //ReadJSONPoints2DVector(*mesh_node, "post_displacements");
                    cache_data.setPostDisplacements(read_pts);
                }

                cache_list.add(cache_data);
            }

            int set_index = cache_manager.getIndexByTime(cur_time);
            cache_manager.displacement_cache_table.set(set_index, cache_list);
        }

        cache_manager.makeAllReady();
    }

    public static void FillUVSwapCache(JsonValue json_obj,
                                       String key,
                                       int start_time,
                                       int end_time,
                                       MeshUVWarpCacheManager cache_manager) {
        JsonValue base_obj = json_obj.get(key);

        cache_manager.init(start_time, end_time);

        for (JsonValue cur_node = base_obj.child; cur_node != null; cur_node = cur_node.next) {
            int cur_time = Integer.parseInt(cur_node.name);

            Vector<MeshUVWarpCache> cache_list = new Vector<MeshUVWarpCache>();

            for (JsonValue uv_node = cur_node.child; uv_node != null; uv_node = uv_node.next)

            {
                String cur_name = uv_node.name;

                MeshUVWarpCache cache_data = new MeshUVWarpCache(cur_name);
                boolean use_uv = ReadBoolJSON(uv_node, "enabled"); //GetJSONNodeFromKey(*uv_node, "enabled")->value.toBool();
                cache_data.setEnabled(use_uv);
                if (use_uv) {
                    Vector2 local_offset = ReadVector2JSON(uv_node, "local_offset"); //ReadJSONVec2(*uv_node, "local_offset");
                    Vector2 global_offset = ReadVector2JSON(uv_node, "global_offset"); //ReadJSONVec2(*uv_node, "global_offset");
                    Vector2 scale = ReadVector2JSON(uv_node, "scale"); //ReadJSONVec2(*uv_node, "scale");
                    cache_data.setUvWarpLocalOffset(local_offset);
                    cache_data.setUvWarpGlobalOffset(global_offset);
                    cache_data.setUvWarpScale(scale);
                }

                cache_list.add(cache_data);
            }

            int set_index = cache_manager.getIndexByTime(cur_time);
            cache_manager.uv_cache_table.set(set_index, cache_list);
        }

        cache_manager.makeAllReady();
    }


}
