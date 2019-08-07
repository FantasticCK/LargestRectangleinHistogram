package com.CK;

import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        int[] heights = {2, 1, 5, 6, 2, 3};
        Solution solution = new Solution();
        System.out.println(solution.largestRectangleArea(heights));
    }
}

class Solution {
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