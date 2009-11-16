# normal member function
class DefnCode
  def init
    #pp self
    #exit
    @t=tmpVar
  end

  def before
    []
  end

  def code
    
    #STDERR.puts "BODY:",@body.body.class
    pp "BO",@body.inspect unless @body.value.is_a?(String)
    
    cm=curModule
    if cm=="root"
      cm="self@"
    else
      cm+="."
    end
    b=@args.before
    as=@args.value.gsub("&","")
    b+
    bgnws(@t,[as],@body.code,@body.value)+
      
    s("#{cm}#{@name}=#{@t}")
  end

  def after
    s("clear #{@t}")
  end
end