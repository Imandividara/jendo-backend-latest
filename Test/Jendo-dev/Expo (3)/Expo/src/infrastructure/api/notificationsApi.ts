import { httpClient } from './httpClient';
import { API_ENDPOINTS } from './endpoints';

export interface NotificationResponse {
  id: number;
  userId: number;
  message: string;
  type: string;
  isRead: boolean;
  createdAt: string;
}

export interface ApiNotificationResponse {
  success: boolean;
  data: NotificationResponse | NotificationResponse[] | { content: NotificationResponse[] } | number;
  message?: string;
}

export const notificationsApi = {
  getByUserId: async (userId: number, page: number = 0, size: number = 20): Promise<NotificationResponse[]> => {
    try {
      console.log('[Notifications] Fetching for user:', userId);
      const response = await httpClient.get<ApiNotificationResponse>(
        `${API_ENDPOINTS.NOTIFICATIONS.BASE}/user/${userId}?page=${page}&size=${size}`
      );
      console.log('[Notifications] Response:', JSON.stringify(response, null, 2));
      if (response.success && response.data) {
        if (Array.isArray(response.data)) {
          console.log('[Notifications] Data is array, count:', response.data.length);
          return response.data;
        }
        const paginated = response.data as { content?: NotificationResponse[] };
        console.log('[Notifications] Data is paginated, count:', paginated.content?.length || 0);
        return paginated.content || [];
      }
      console.log('[Notifications] No data or not successful');
      return [];
    } catch (error) {
      console.error('[Notifications] Error fetching:', error);
      return [];
    }
  },

  getUnread: async (userId: number): Promise<NotificationResponse[]> => {
    try {
      const response = await httpClient.get<ApiNotificationResponse>(
        `${API_ENDPOINTS.NOTIFICATIONS.BASE}/user/${userId}/unread`
      );
      if (response.success && response.data) {
        return response.data as NotificationResponse[];
      }
      return [];
    } catch (error) {
      console.error('Error fetching unread notifications:', error);
      return [];
    }
  },

  getUnreadCount: async (userId: number): Promise<number> => {
    try {
      const response = await httpClient.get<ApiNotificationResponse>(
        `${API_ENDPOINTS.NOTIFICATIONS.BASE}/user/${userId}/unread/count`
      );
      if (response.success && response.data !== undefined) {
        return response.data as number;
      }
      return 0;
    } catch (error) {
      console.error('Error fetching unread count:', error);
      return 0;
    }
  },

  markAsRead: async (notificationId: number): Promise<NotificationResponse | null> => {
    try {
      const response = await httpClient.patch<ApiNotificationResponse>(
        API_ENDPOINTS.NOTIFICATIONS.MARK_READ(String(notificationId))
      );
      if (response.success && response.data) {
        return response.data as NotificationResponse;
      }
      return null;
    } catch (error) {
      console.error('Error marking notification as read:', error);
      return null;
    }
  },

  markAllAsRead: async (userId: number): Promise<boolean> => {
    try {
      const response = await httpClient.patch<ApiNotificationResponse>(
        `${API_ENDPOINTS.NOTIFICATIONS.BASE}/user/${userId}/read-all`
      );
      return response.success;
    } catch (error) {
      console.error('Error marking all as read:', error);
      return false;
    }
  },

  delete: async (notificationId: number): Promise<boolean> => {
    try {
      await httpClient.delete(`${API_ENDPOINTS.NOTIFICATIONS.BASE}/${notificationId}`);
      return true;
    } catch (error) {
      console.error('Error deleting notification:', error);
      return false;
    }
  },
};
