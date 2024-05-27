/*
Good afternoon. The solution with BitSet has been posted on Habr for two years now, so I applied my own approach.
Basically, we have a tree of Lists with every node having 256 children up to the 4th generation.
The trie structure ensures that we check each combination for uniqueness in no more than 1024 tries.
Processing a 120 GB test file takes about 100 minutes and is successful. Thank you! :)
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicLong;

public class UniqueCounter {
    private static class TreeNode {
        short value;
        List<TreeNode> nodes;

        public TreeNode(short value, List<TreeNode> nodes) {
            this.value = value;
            this.nodes = nodes;
        }
    }

    public static void main(String[] args) throws IOException {
        AtomicLong counter = new AtomicLong(0L);
        List<TreeNode> result = new ArrayList<>(256);

        String inputFile = "D:\\ip_addresses.txt";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ".");
                List<TreeNode> currentList = result;
                currentList = addNewNode(Short.parseShort(st.nextToken()), currentList, false, counter);
                currentList = addNewNode(Short.parseShort(st.nextToken()), currentList, false, counter);
                currentList = addNewNode(Short.parseShort(st.nextToken()), currentList, false, counter);
                currentList = addNewNode(Short.parseShort(st.nextToken()), currentList, true, counter);
            }
        }
        System.out.println("The number of the unique IP addresses in the file is: " + counter);
    }

    private static List<TreeNode> addNewNode(short value, List<TreeNode> currentList, boolean isLastLvl, AtomicLong count) {
        if (currentList.isEmpty()) {
            TreeNode newNode = new TreeNode(value, isLastLvl ? null : new ArrayList<>(256));
            currentList.add(newNode);
            currentList = newNode.nodes;
            if (isLastLvl) {
                count.incrementAndGet();
            }
        } else {
            TreeNode currentNode = currentList.stream().filter(node -> node.value == value).findFirst().orElse(null);
            if (currentNode == null) {
                TreeNode node = new TreeNode(value, isLastLvl ? null : new ArrayList<>(256));
                currentList.add(node);
                currentList = node.nodes;
                if (isLastLvl) {
                    count.incrementAndGet();
                }
            } else {
                currentList = currentNode.nodes;
            }
        }
        return currentList;
    }
}