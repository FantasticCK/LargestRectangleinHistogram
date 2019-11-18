package com.CK;

import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        int[] heights = {6, 7, 5, 2, 4, 5, 9, 3};
        Solution solution = new Solution();
        System.out.println(solution.largestRectangleArea(heights));
    }
}

class Solution2 {
    public int largestRectangleArea(int[] heights) {
        if (heights.length == 0) return 0;
        Stack<Integer> stack = new Stack<>();
        int currentIdx, left, right = 0, max = Integer.MIN_VALUE, area;
        for (int i = 0; i < heights.length; i++) {
            if (stack.isEmpty() || heights[i] >= heights[stack.peek()]) stack.push(i);
            else {
                currentIdx = stack.pop();
                right = i;
                left = stack.isEmpty() ? -1 : stack.peek();
                area = heights[currentIdx] * (right - left - 1);
                max = Math.max(max, area);
                i--;
            }
        }
        right = stack.peek() + 1;
        while (!stack.isEmpty()) {
            currentIdx = stack.pop();
            left = stack.isEmpty() ? -1 : stack.peek();
            area = heights[currentIdx] * (right - left - 1);
            max = Math.max(max, area);
        }
        return max;
    }
}

class Solution3 {
    public static int largestRectangleArea(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }
        int[] lessFromLeft = new int[height.length]; // idx of the first bar the left that is lower than current
        int[] lessFromRight = new int[height.length]; // idx of the first bar the right that is lower than current
        lessFromRight[height.length - 1] = height.length;
        lessFromLeft[0] = -1;

        for (int i = 1; i < height.length; i++) {
            int p = i - 1;

            while (p >= 0 && height[p] >= height[i]) {
                p = lessFromLeft[p];
            }
            lessFromLeft[i] = p;
        }

        for (int i = height.length - 2; i >= 0; i--) {
            int p = i + 1;

            while (p < height.length && height[p] >= height[i]) {
                p = lessFromRight[p];
            }
            lessFromRight[i] = p;
        }

        int maxArea = 0;
        for (int i = 0; i < height.length; i++) {
            maxArea = Math.max(maxArea, height[i] * (lessFromRight[i] - lessFromLeft[i] - 1));
        }

        return maxArea;
    }
}

//Segment Tree
class Solution {

    class SegTreeNode {
        int start;
        int end;
        int min;
        SegTreeNode left;
        SegTreeNode right;

        SegTreeNode(int start, int end) {
            min = 0;
            this.start = start;
            this.end = end;
            left = right = null;
        }
    }

    public int largestRectangleArea(int[] heights) {
        if (heights.length == 0) return 0;
        // first build a segment tree
        SegTreeNode root = buildSegmentTree(heights, 0, heights.length - 1);
        // next calculate the maximum area recursively
        return calculateMax(heights, root, 0, heights.length - 1);
    }

    public int calculateMax(int[] heights, SegTreeNode root, int start, int end) {
        if (start > end) {
            return -1;
        }
        if (start == end) {
            return heights[start];
        }
        int minIndex = query(root, heights, start, end);
        int leftMax = calculateMax(heights, root, start, minIndex - 1);
        int rightMax = calculateMax(heights, root, minIndex + 1, end);
        int minMax = heights[minIndex] * (end - start + 1);
        return Math.max(Math.max(leftMax, rightMax), minMax);
    }

    public SegTreeNode buildSegmentTree(int[] heights, int start, int end) {
        if (start > end) return null;
        SegTreeNode root = new SegTreeNode(start, end);
        if (start == end) {
            root.min = start;
            return root;
        } else {
            int middle = (start + end) / 2;
            root.left = buildSegmentTree(heights, start, middle);
            root.right = buildSegmentTree(heights, middle + 1, end);
            root.min = heights[root.left.min] < heights[root.right.min] ? root.left.min : root.right.min;
            return root;
        }
    }

    int query(SegTreeNode root, int[] heights, int start, int end) {
        if (root == null || end < root.start || start > root.end) return -1;
        if (start <= root.start && end >= root.end) {
            return root.min;
        }
        int leftMin = query(root.left, heights, start, end);
        int rightMin = query(root.right, heights, start, end);
        if (leftMin == -1) return rightMin;
        if (rightMin == -1) return leftMin;
        return heights[leftMin] < heights[rightMin] ? leftMin : rightMin;
    }
}