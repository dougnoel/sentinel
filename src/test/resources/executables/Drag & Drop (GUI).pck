GDPC                  0                                                                      	   \   res://.godot/exported/133200997/export-2f4824c7e41d4d363dda925881a7e4a8-drag_and_drop.scn           �
      �
e���]��G    ,   res://.godot/global_script_class_cache.cfg                 ��Р�8���8~$}P�    D   res://.godot/imported/icon.png-487276ed1e3a0c39cad0279d744ee560.ctex�      .      >�Ard����(R ����       res://.godot/uid_cache.bin  �      B       �S�_�-4F�A��b        res://drag_and_drop.tscn.remap  �      j       zE���<u3h2��$-       res://drag_drop_script.gd   �
      �      �8Zi�'�{l���       res://icon.png  P      &      ��������o�k=       res://icon.png.import   �      �      �D� �\��i0���w       res://project.binary�      -      ~�H�����Q7CC<��    Kӊlist=Array[Dictionary]([])
ם�RSRC                     PackedScene            ��������                                                  resource_local_to_scene    resource_name 	   _bundled    script       Script    res://drag_drop_script.gd ��������      local://PackedScene_dd6c4          PackedScene          	         names "   $      DragAndDrop    layout_mode    anchors_preset    anchor_left    anchor_top    anchor_right    anchor_bottom    offset_left    offset_top    offset_right    offset_bottom    grow_horizontal    grow_vertical    pivot_offset    size_flags_horizontal    size_flags_vertical    Control    Information    text    Label    GridContainer    columns    ColorPickerButton0    custom_minimum_size    mouse_default_cursor_shape    color    script    ColorPickerButton    ColorPickerButton1    ColorPickerButton2    ColorPickerButton3    ColorPickerButton4    ColorPickerButton5    ColorPickerButton6    ColorPickerButton7    ColorPickerButton8    	   variants                         ?      �     ��      D     �C      
      D  �C            4�     �   P   Drag colors from button to button, or change button colors and drag them again.      z�     zC     HC
     �B  �B               ��+?F�?      �?                 �L?��=?  �?   J�>    >"?  �?     �?  �?  �?  �?     �?Eo?      �?   �>��b?L��=  �?   �h?    �Lc?  �?       �A�>      �?           �|6>  �?      node_count             nodes       ��������       ����                                                    	      
                                                      ����      	                                    
   	      
               	                           ����
      	                                       	      
                              ����                                                              ����                                                        ����                                                        ����                                                        ����                                                         ����                                                     !   ����                                                     "   ����                                                     #   ����                                                 conn_count              conns               node_paths              editable_instances              version             RSRC�������n�iy���extends ColorPickerButton

# Returns the data to pass from an object when you click and drag away from
# this object. Also calls set_drag_preview() to show the mouse dragging
# something so the user knows that the operation is working.
func _get_drag_data(_pos):
	# Use another colorpicker as drag preview.
	var cpb = ColorPickerButton.new()
	cpb.color = color
	cpb.size = Vector2(80.0, 50.0)
	
	# Allows us to center the color picker on the mouse
	var preview = Control.new()
	preview.add_child(cpb)
	cpb.position = -0.5 * cpb.size
	
	# Sets what the user will see they are dragging
	set_drag_preview(preview)
	
	# Return color as drag data.
	return color


# Returns a boolean by examining the data being dragged to see if it's valid
# to drop here.
func _can_drop_data(_pos, data):
	return typeof(data) == TYPE_COLOR

# Takes the data being dragged and processes it. In this case, we are
# assigning a new color to the target color picker button.
func _drop_data(_pos, data):
	color = data
�����h��I��EZaGST2   @   @      ����               @ @        �  RIFF�  WEBPVP8L�  /?� 7�mW��EYXظ2#r&2�;���ٶ���%g���&�ݮ"I�����
�a�@ �xgͿ� �6�Q{������Y�4�D���z��������k�����������f���&@(��	*��
$������ "�30������y��"��|�4VF��A*���@*X��j1F�}u�Q�;���7����@�0���&�hY�N����Y�v�###���~vx>@�@ }�� �m;&�v�n��ڶǞ���[c��&7���'��y������	�־��i)�����_�Y������K�.
����=�Q����9��M>��z�GT�9��{��!;D�����[O�G̰���������Q#,��)4Z�F��ݣ!��V=7p�a�_����=����ĉ�*��IH����Q�{;S��a�$��$S�y�:{�Ƣk}�S.��b� ���;�����E����W�/� 9��n��l:��`0�&he�U�X������YزƦ��tF��b6�����333sss�Kg� ͛�x--�M�t�2��N�_/.C
6ހ����4��%��s+ mk�������S�0��޳xJ{�(�`*�m�����<���I�i[�Tk����"��n�8d��}	OE��J�rZ��T���ݞ+x*=�F(�!W�T/�}��J�R؏���rO��O�|�{����& 0[remap]

importer="texture"
type="CompressedTexture2D"
uid="uid://do6kothwldh7q"
path="res://.godot/imported/icon.png-487276ed1e3a0c39cad0279d744ee560.ctex"
metadata={
"vram_texture": false
}

[deps]

source_file="res://icon.png"
dest_files=["res://.godot/imported/icon.png-487276ed1e3a0c39cad0279d744ee560.ctex"]

[params]

compress/mode=0
compress/high_quality=false
compress/lossy_quality=0.7
compress/hdr_compression=1
compress/normal_map=0
compress/channel_pack=0
mipmaps/generate=false
mipmaps/limit=-1
roughness/mode=0
roughness/src_normal=""
process/fix_alpha_border=true
process/premult_alpha=false
process/normal_map_invert_y=false
process/hdr_as_srgb=false
process/hdr_clamp_exposure=false
process/size_limit=0
detect_3d/compress_to=1
����E�[remap]

path="res://.godot/exported/133200997/export-2f4824c7e41d4d363dda925881a7e4a8-drag_and_drop.scn"
8k�����PNG

   IHDR   @   @   %�   	pHYs  �  ��+   tIME�!(�A��  �IDATh��Z���@��8�D9H	��? �BE�h))� 
:(�3(�� $$P Pt';��Qd:���Z«)ly��{�3�����p�V��=Jm9��W�_NN$��XE��z� ,�[>�+��D�t��t�L���R�B�cn~�YO�C�����v�퀖�;�9  �ȱ
m�P�:����d'LO;�aPh�:H4�Rl?��E!JgX�N��Pˆ15P�3aF �Ӡ������s���Q}Q;�������v=���y�P�$@l{Gj�NN�$�M���@��;�$���o�=o�,-(���EDx���x�] 7�_�7��XaE �S;;PMgs��O�˯/� ��?��� c�1fbM�_T�@��ܖ�+��L&]�j �Zk-о�qu���g��`0� F #��`P��t��z�(�� ��D�9�eYY��4��`�_P6$�L�a�ߨ�M�l�|5�N�0dYv|t�r��ֺ�
�xh�+������Yk�4)��Y1�,WV�+~�C��:�b-y��M�G_�:�쥈��2�6��ϸk���m'�"����?+E=\�0��N÷hU�T���	�{�#,�R�����^�J�T���8,b �8�����B���PC��3"�(��AF�ڸ��9jPϛ)IPG�!PHA8�P����VJ�UЂ|�����4�P}�c�c[E��nr�_��� �,    IEND�B`�e>�����y�   ���2z&   res://drag_and_drop.tscn45{��w�q   res://icon.pnga(��%t�u*��ECFG      application/config/name         Drag & Drop (GUI)      application/config/description�      �   A demo showcasing drag and drop functionality.

- Drag and drop the color buttons to copy their colors over.
- Click on the buttons to manually adjust their color.    application/run/main_scene          res://drag_and_drop.tscn   application/config/features   "         4.0    application/config/icon         res://icon.png     display/window/stretch/mode         canvas_items   display/window/stretch/aspect         expand  �j�