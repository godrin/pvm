def a
  raise 1
end
x=begin
  a
rescue 
end
# tmp4=begin_withscope()
#   tmp1=1
#   tmp2=tmp1
#   ereturn tmp2
# end
# self@a=tmp4
# clear tmp4
# try
#   tmp5=a()
#   tmp7=tmp5
# rescue
#   tmp7=nil
# end
# x=tmp7
# clear tmp7