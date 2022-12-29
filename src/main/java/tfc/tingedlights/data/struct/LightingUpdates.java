package tfc.tingedlights.data.struct;

import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import tfc.tingedlights.data.FastUtil;

import java.util.Collection;
import java.util.Set;

public class LightingUpdates {
	public final Collection<LightNode>[] freshNodes = new Set[15];
	public Collection<LightNode>[] newNodes = new Set[15];
	public Collection<LightNode>[] addedNodes = new Set[15];
	
	public LightingUpdates() {
		for (int i = 0; i < freshNodes.length; i++) {
			freshNodes[i] = new ObjectOpenCustomHashSet<>(FastUtil.nodeStrategy);
			newNodes[i] = new ObjectOpenCustomHashSet<>(FastUtil.nodeStrategy);
//			newNodes[i] = new ObjectArraySet<>();
			addedNodes[i] = new ObjectOpenCustomHashSet<>(FastUtil.nodeStrategy);
		}
	}
	
	public void addFresh(LightNode node) {
		freshNodes[node.brightness() - 1].add(node);
	}
	
	public void swap() {
		for (int i = 0; i < freshNodes.length; i++) {
			Collection<LightNode> freshNodes = this.freshNodes[i];
			Collection<LightNode> newNodes = this.newNodes[i];
			if (!freshNodes.isEmpty()) {
				synchronized (this.freshNodes) {
					newNodes.addAll(freshNodes);
					freshNodes.clear();
				}
			}
		}
	}
	
	public boolean hasAny() {
		for (Collection<LightNode> newNode : newNodes)
			if (!newNode.isEmpty()) return true;
		return false;
	}
	
	public void removeAll(Set<LightNode> newlyRemovedNodes) {
		for (Collection<LightNode> newNode : newNodes) {
			newNode.removeAll(newlyRemovedNodes);
		}
	}
	
	public boolean splitWorkload() {
		for (int i = 5; i < 15; i++) {
			if (!newNodes[i].isEmpty()) return false;
		}
		
		int totalRemaining = 0;
		for (int i = 0; i <= 5; i++) {
			totalRemaining += newNodes[i].size();
		}
		return totalRemaining <= 10000;
	}
	
	public boolean allowReversal() {
		int totalRemaining = 0;
		for (int i = 0; i < 15; i++) {
			totalRemaining += newNodes[i].size();
		}
		return totalRemaining >= 30000000;
	}
}
