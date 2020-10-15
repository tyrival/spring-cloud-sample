package com.acrel.entity.base;

import com.google.gson.Gson;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 树节点类
 * @Author: ZhouChenyu
 */
@Data
public class TreeNode<T extends TreeNode> extends Base {

    /**
     * 父节点ID
     */
    private Integer parentId;

    /**
     * 子节点列表
     */
    private List<T> children;

    public TreeNode() {
        this.children = new ArrayList<>();
    }

    /**
     * 将节点列表转换为菜单树，可能不止一棵
     * @param nodeList
     * @return
     */
    public static <T extends TreeNode> List<T> formatTree(List<T> nodeList) {
        Map<String, TreeNode> map = new HashMap<>(nodeList.size());
        List<TreeNode> rootList = new ArrayList<>();
        for (int i = 0; i < nodeList.size(); i++) {
            TreeNode node = nodeList.get(i);
            map.put(String.valueOf(node.getId()), node);
        }
        for (int i = 0; i < nodeList.size(); i++) {
            TreeNode node = nodeList.get(i);
            Integer parentId = node.getParentId();
            if (StringUtils.isEmpty(parentId)) {
                rootList.add(node);
            } else {
                TreeNode parent = map.get(parentId);
                if(parent == null) {
                    rootList.add(node);
                }else{
                    parent.addChild(node);
                }
            }
        }
        return (List<T>) rootList;
    }

    /**
     * 获取某个节点及其下属节点构成的树
     * @param nodeList
     * @return
     */
    public static <T extends TreeNode> List<T> formatTree(List<T> nodeList, String rootId) {
        if (StringUtils.isEmpty(rootId)) {
            return formatTree(nodeList);
        }
        Map<String, TreeNode> map = new HashMap<>();
        List<TreeNode> rootList = new ArrayList<>();
        for (int i = 0; i < nodeList.size(); i++) {
            TreeNode node = nodeList.get(i);
            map.put(String.valueOf(node.getId()), node);
        }
        for (int i = 0; i < nodeList.size(); i++) {
            TreeNode node = nodeList.get(i);
            Integer parentId = node.getParentId();
            if (rootId.equals(node.getId())) {
                rootList.add(node);
            } else {
                TreeNode parent = map.get(parentId);
                if (parent == null) {
                    continue;
                }
                parent.addChild(node);
            }
        }
        return (List<T>) rootList;
    }

    /**
     * 添加子节点
     * @param node
     */
    public void addChild(T node) {
        this.children.add(node);
    }

    /**
     * 根据id移除子节点
     * @param id
     */
    public void removeChild(String id) {
        if (StringUtils.isEmpty(id)) {
            return;
        }
        for (int i = 0; i < this.children.size(); i++) {
            if (id.equals(this.children.get(i).getId())) {
                this.children.remove(i);
                return;
            }
        }
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
