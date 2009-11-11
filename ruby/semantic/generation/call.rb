class CallCode
  def init
    assert{filename}
    @ret=tmpVar
  end

  def before
    []
  end

  def after
    s("clear #{@ret}")
  end

  def code(addargs=[])
    addargs=addargs.join(", ")
    args=@args.value
    assert{args=~/\[.*\]/}
    args=args[1..-2]
    l=""
    l=@left.value.to_s+"." if @left
    addargs=", "+addargs if addargs.length>0 and args.length>0
    @args.before+s("#{@ret}=#{l}#{@name}(#{args}#{addargs})")+@args.after
  end

  def value
    @ret
  end
end