/**
 * 事件监听区，包括本地监听（通过Spring Event Listener）和远程监听（通过Spring Cloud Stream）。
 * 
 * Event's listener area, including local listeners (via Spring Event Listener) and remote listeners (via Spring Cloud
 * Stream)
 * 
 * <p>
 * 注意：有一致性要求的领域事件，必须被远程监听；只有订阅方的处理过程（而不仅仅是消息的接受）完全幂等时，才可以额外地添加本地监听。
 * 
 * Note: Domain events with consistency requirements must be remotely listened; local listeners can be added
 * additionally only if the subscriber's processing (not just the message's receiving) is completely idempotent.
 * 
 * @author Liu Xiaolei
 * @date 2018/08/27
 */
package xyz.devlxl.chaos.s1.application.listener;