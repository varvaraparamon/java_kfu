FROM tomcat:9.0

# 1. Копируем стандартные приложения (manager, host-manager, docs и т.д.) в рабочую директорию
RUN cp -R /usr/local/tomcat/webapps.dist/* /usr/local/tomcat/webapps/

# 2. Добавляем нужные роли и пользователя admin:admin в конфигурацию tomcat-users.xml
RUN sed -i '/<\/tomcat-users>/i \
  <role rolename="manager-gui"/>\n\
  <role rolename="admin-gui"/>\n\
  <user username="admin" password="admin" roles="manager-gui,admin-gui"/>' /usr/local/tomcat/conf/tomcat-users.xml

# 3. Отключаем фильтрацию по IP (RemoteAddrValve), чтобы можно было заходить в Manager GUI с хост-машины
RUN echo '<Context antiResourceLocking="false" privileged="true" />' > /usr/local/tomcat/webapps/manager/META-INF/context.xml && \
    echo '<Context antiResourceLocking="false" privileged="true" />' > /usr/local/tomcat/webapps/host-manager/META-INF/context.xml