package tfc.tingedlights.data.struct;

import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import tfc.tingedlights.data.FastUtil;

import java.util.Set;

public class LightingUpdates {
	public final Set<LightNode>[] freshNodes = new Set[15];
	public Set<LightNode>[] newNodes = new Set[15];
	public Set<LightNode>[] addedNodes = new Set[15];
	
	public LightingUpdates() {
		for (int i = 0; i < freshNodes.length; i++) {
			freshNodes[i] = new ObjectOpenCustomHashSet<>(FastUtil.nodeStrategy);
			newNodes[i] = new ObjectOpenCustomHashSet<>(FastUtil.nodeStrategy);
			addedNodes[i] = new ObjectOpenCustomHashSet<>(FastUtil.nodeStrategy);
		}
	}
	
	public void addFresh(LightNode node) {
		freshNodes[node.brightness() - 1].add(node);
	}
	
	public void swap() {
		for (int i = 0; i < freshNodes.length; i++) {
			Set<LightNode> freshNodes = this.freshNodes[i];
			Set<LightNode> newNodes = this.newNodes[i];
			if (!freshNodes.isEmpty()) {
				synchronized (this.freshNodes) {
					newNodes.addAll(freshNodes);
					freshNodes.clear();
				}
			}
		}
	}
	
	public boolean hasAny() {
		for (Set<LightNode> newNode : newNodes)
			if (!newNode.isEmpty()) return true;
		return false;
	}
	
	public void removeAll(Set<LightNode> newlyRemovedNodes) {
		for (Set<LightNode> newNode : newNodes) {
			newNode.removeAll(newlyRemovedNodes);
		}
	}
}
