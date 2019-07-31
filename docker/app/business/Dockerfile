from java
# 修改hosts，此方法不起作用
#run cp /etc/hosts /etc/hoststmp && \
#    sed -i '1a 39.106.73.92    mongo' /etc/hoststmp && \
#    cat /etc/hoststmp > /etc/hosts
entrypoint echo 39.106.73.92  mongo >> /etc/hosts && \
          java -jar /app/expshare-business.jar --spring.profiles.active=prod && \
          tail -f /etc/profile
